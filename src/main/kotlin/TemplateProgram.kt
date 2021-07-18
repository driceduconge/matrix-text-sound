import ddf.minim.Minim
import ddf.minim.ugens.Oscil
import ddf.minim.ugens.Waves
import org.openrndr.*
import org.openrndr.color.rgb
import org.openrndr.draw.loadFont
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import kotlin.math.pow
import kotlin.math.sqrt
import Oscillator

var oneSelected = false
var cellEcted = 0
var array = mutableListOf<Section>()

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
    var colorFill = rgb(0.9)
    fun updateColor() {
        colorFill = if (selected) {
            rgb(0.0,1.0,0.0)
        } else {
            rgb(0.9)
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
        var wave = Oscil(440.0f, 0.5f, Waves.SINE)
        wave.patch(out)
        //var aaaa = Oscillator()
        val font = loadFont("data/fonts/RuneScape-Surok.ttf",20.0)
        var a = 0
        //créer chaque beat
        for(j in 1..8) {
            for (i in 1..24) {
                a++
                var col = Section(i, j - 1, a)
                array.add(col)
            }
        }
        var click1 = 0.0
        var click2 = 0.0

        mouse.buttonDown.listen{
            click1 = it.position.x
            click2 = it.position.y
        }
        var cellNum = 0


        program.ended.listen{
            minim.stop()
        }
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

        extend {

            var color = rgb(0.0)
            drawer.clear(color)
            //drawer.stroke = rgb(255.0)
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

            }

        }

    }

}

