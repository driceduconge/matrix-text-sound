import ddf.minim.Minim
import ddf.minim.ugens.Waves.*
import org.openrndr.*
import org.openrndr.color.rgb
import org.openrndr.draw.loadFont
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import kotlin.math.pow
import kotlin.math.sqrt


var oneSelected = false
var cellEcted = 0
var array = mutableListOf<Section>()
//function map copied from https://stackoverflow.com/questions/17134839/how-does-the-map-function-in-processing-work
fun map(value: Float, istart: Float, istop: Float, ostart: Float, ostop: Float):Float {
    return ostart + (ostop - ostart) * ((value - istart) / (istop - istart))
}

fun distance(xa:Double,ya:Double,xb:Double,yb:Double):Double{
    val x = xa-xb
    val y = ya-yb
    val result = sqrt(x.pow(2)+y.pow(2))
    return result
}
fun mouseCollision(x:Double,y:Double,width:Double,height:Double,mouseX:Double,mouseY:Double):Boolean{
    return mouseX>x && mouseX<x+width && mouseY>y && mouseY<y+height
}
fun select(icell:Int, sec: Section){
    sec.selected = true
    oneSelected = true
    cellEcted = icell
    for(all in array){
        if(all!=sec){
            all.selected = false
        }
    }
}

class Section(col:Int, line:Int, cell:Int){
    val numberSt = cell.toString()
    var line = line
    var col = col
    var cell = cell
    var secheight = 60.0
    var secwidth = 15.0
    var secX = 15.0+(15.0*col)
    var colY = 30.0+(line*80.0)
    var letter = ' '
    var selected = false
    var playing = false
    var colorFill = rgb(0.9)
    fun updateColor() {
        if (selected) {
            colorFill = rgb(0.0,1.0,0.0)
        }
        else if(playing) {
            colorFill = rgb(1.0,0.0,1.0)
        }
        else {
            colorFill = rgb(0.9)
        }
        }//updates color depending on selection
    fun updateLetter() {
        if(letter != '=' && letter != '-' && letter!=' '){
        }
        else {
            if (cell % 2 == 0) {
                letter = '='

            } else {
                letter = '-' //PUREE JE GALERE
            }
        }
    }
    fun playLetter(cellNum:Int): Float {
        playing = cellNum==cell
        if(letter != '=' && letter != '-'){
            return map(this.letter.code.toFloat(),65.0f,90.0f,123.0f,587.0f)
        }
        else{return 0.0f}
    }
}
fun stopped(times:Int):Boolean{
    return times != 0
}


fun main() = application {

    configure {
        width = 1000
        height = 690

    }

    program {

        var minim = Minim(object : Object() {

            public fun sketchPath(fileName: String): String {
                return fileName
            }

            public fun createInput(fileName: String): InputStream {
                return FileInputStream(File(fileName))
            }

        })
        var out = minim.lineOut

/*
        var out = minim.lineOut
        var wave = Oscil(440.0f, 0.5f, Waves.SINE)
        wave.patch(out)*/


        val font = loadFont("data/fonts/RuneScape-Surok.ttf",20.0)
        val font2 = loadFont("data/fonts/RuneScape-Plain-12.ttf",30.0)
        val font3 = loadFont("data/fonts/RuneScape-Plain-12.ttf",25.0)

        var a = 0
        //créer chaque note
        for(j in 1..8) {
            for (i in 1..24) {
                a++
                var col = Section(i, j - 1, a)
                array.add(col)
            }
        }
        var click1 = 0.0

        var click2 = 0.0
        var clicked = false
        mouse.buttonDown.listen{
            click1 = it.position.x
            click2 = it.position.y
            clicked = true
        }
        var cellNum = 0

        var up = false
        var down = false
        var space = 0
        program.ended.listen{
            minim.stop()
        }

        var frequencyeah = 0.0f


        var newfreq2 = 0.0f
        var posx = 0.0
        var posy = 0.0
        mouse.moved.listen{
           // newfreq = mouse.position.x.toFloat()
            newfreq2 = mouse.position.y.toFloat()
            posx = mouse.position.x
            posy = mouse.position.y

        }
        keyboard.character.listen {
            if(oneSelected){
                if (it.character.toInt() == 32 || it.character.toInt() in 65..90 || it.character.toInt() in 97..122) {
                    array[cellEcted - 1].letter = it.character.toUpperCase()
                }
                else {
                }
            }
            else {
                if (it.character.toInt() == 32 || it.character.toInt() in 65..90 || it.character.toInt() in 97..122) {
                    array[cellNum].letter = it.character.toUpperCase()
                    cellNum += 1
                } else {
                    cellNum += 1
                }
            }

        }
        var selectLFO = 0
        var ampLFO1 = 40.0f
        var freLFO1 = 100.0f
        var wavef = SINE
        var celln = 0

        var psc = Oscillator(frequencyeah,10.0f,wavef,freLFO1,ampLFO1)

        keyboard.keyDown.listen{
            if (it.key == KEY_ESCAPE) {

                for(i in array){
                    i.selected = false
                    oneSelected = false
                    cellEcted = 0
                }
            }
            if(it.key == KEY_BACKSPACE){
                cellNum-=1
                array[cellNum].letter = ' '
            }
            if(it.key == KEY_ARROW_UP){
                up = true
            }
            if(it.key == KEY_ARROW_DOWN){
                down = true
            }
            if(it.key == KEY_ENTER){
                if(space==0){
                    space=1
                    //playRythm(160)
                }
                else{
                    space=0
                    for(all in array){
                        all.playing = false
                        celln = 0
                    }
                }
            }
        }

        extend {
            if(frameCount%120.0==0.0){
                print("change done")
                minim.stop()
                out = minim.lineOut
                psc = Oscillator(frequencyeah,10.0f,wavef,freLFO1,ampLFO1)
            }
            var color = rgb(0.0)
            drawer.clear(color)
            //drawer.stroke = rgb(255.0)
            // OSC AND LFO !!!!!!!!!!!!!!!!!!!!!!!!!!!
            drawer.fontMap = font2
            drawer.text("OSC 1",450.0,60.0)
            drawer.fontMap = font3
            drawer.fill = rgb(1.0,1.0,1.0)
            if(mouseCollision(450.0,80.0,30.0,20.0,posx,posy)){
                drawer.text("tri",450.0,110.0)
                drawer.text("squ",450.0,130.0)
                drawer.text("saw",450.0,150.0)
                drawer.fill = rgb(0.0,1.0,0.0)
                drawer.text("sin",450.0,90.0)
                if(clicked){
                wavef = SINE
                }
            }
            else if(mouseCollision(450.0,100.0,30.0,20.0,posx,posy)){
                drawer.text("sin",450.0,90.0)
                drawer.text("squ",450.0,130.0)
                drawer.text("saw",450.0,150.0)
                drawer.fill = rgb(0.0,1.0,0.0)
                drawer.text("tri",450.0,110.0)
                if(clicked){
                    wavef = TRIANGLE
                }
            }
            else if(mouseCollision(450.0,120.0,30.0,20.0,posx,posy)){
                drawer.text("saw",450.0,150.0)
                drawer.text("sin",450.0,90.0)
                drawer.text("tri",450.0,110.0)
                drawer.fill = rgb(0.0,1.0,0.0)
                drawer.text("squ",450.0,130.0)
                if(clicked){
                    wavef = SQUARE
                }
            }
            else if(mouseCollision(450.0,140.0,30.0,20.0,posx,posy)){
                drawer.text("squ",450.0,130.0)
                drawer.text("sin",450.0,90.0)
                drawer.text("tri",450.0,110.0)
                drawer.fill = rgb(0.0,1.0,0.0)
                drawer.text("saw",450.0,150.0)
                if(clicked){
                    wavef = SAW
                    println("SAW")
                }
            }
            else{
                drawer.text("squ",450.0,130.0)
                drawer.text("sin",450.0,90.0)
                drawer.text("tri",450.0,110.0)
                drawer.text("saw",450.0,150.0)
            }
            drawer.text("LFO 1",450.0,180.0)
            drawer.text("AMP",450.0,210.0)
            drawer.text("FRE",450.0,230.0)

            if(mouseCollision(450.0,200.0,30.0,20.0,posx,posy)){
                drawer.text("FRE",450.0,230.0)
                drawer.fill = rgb(0.0,1.0,0.0)
                drawer.text("AMP",450.0,210.0)
                if(clicked){
                    selectLFO = 1
                }
            }
            else if(mouseCollision(450.0,220.0,30.0,20.0,posx,posy)){
                drawer.text("AMP",450.0,210.0)
                drawer.fill = rgb(0.0,1.0,0.0)
                drawer.text("FRE",450.0,230.0)
                if(clicked){
                    selectLFO = 2
                }
            }
            if(selectLFO == 1){
                if(up){
                    ampLFO1 += 20.0f
                }
                else if(down){
                    ampLFO1 -= 20.0f
                }
                drawer.text("FRE",450.0,230.0)
                drawer.text(freLFO1.toString(),490.0,230.0)
                drawer.fill = rgb(0.0,1.0,0.0)
                drawer.text("AMP",450.0,210.0)
                drawer.text(ampLFO1.toString(),490.0,210.0)

            }
            else if(selectLFO == 2){
                if(up){
                    freLFO1 += 20.0f
                }
                else if(down){
                    freLFO1 -= 20.0f
                }
                drawer.text("AMP",450.0,210.0)
                drawer.text(ampLFO1.toString(),490.0,210.0)
                drawer.text("FRE",450.0,230.0)
                drawer.text(freLFO1.toString(),490.0,230.0)
            }
            else{
                drawer.fill = rgb(1.0,1.0,1.0)
                drawer.text(ampLFO1.toString(),490.0,210.0)
                drawer.text(freLFO1.toString(),490.0,230.0)
            }
                        // OSC AND LFO !!!!!!!!!!!!!!!!!!!!!!!!!!!
            for(i in array){
                i.updateColor()
                i.updateLetter()
                drawer.strokeWeight = 0.001
                drawer.stroke = rgb(0.1,0.1,0.1)
                if(i.line!=0) {
                    drawer.lineSegment(30.0, i.colY - 15.0, 390.0, i.colY - 15.0)
                }
                if(mouseCollision(i.secX, i.colY, i.secwidth, i.secheight, click1, click2)){
                    select(i.cell, i)
                    //on reset les positions de clicks
                    click1 = 0.0
                    click2 = 0.0
                }//selection
                if(mouseCollision(i.secX, i.colY, i.secwidth, i.secheight, mouse.position.x, mouse.position.y)){
                    drawer.fill = rgb(0.0,1.0,1.0)
                }//hover
                else{drawer.fill = i.colorFill}
               // drawer.rectangle(i.secX,i.colY,i.secwidth,i.secheight)
                drawer.fontMap = font
                drawer.text(i.letter.toString(),5.0+i.secX,30.0+i.colY)

            }//zone de saisie
            if(space==1){
                println(frequencyeah)
                psc.adsr.patch(out)
                if(frameCount%30.0==0.0) {
                    psc.adsr.noteOff()
                    println(frequencyeah)
                    psc.adsr.noteOff()
                    var argg = array[celln].playLetter(array[celln].cell)
                    if (argg != 0.0f) {
                        frequencyeah = argg
                        psc.updateWave(ampLFO1,freLFO1,frequencyeah,wavef)
                        psc.adsr.noteOn()
                    }
                    celln+=1
                }


            }
            clicked = false
            up = false
            down = false
        }

    }

}

