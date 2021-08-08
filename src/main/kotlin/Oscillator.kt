//import ddf.minim.*

import ddf.minim.AudioOutput
import ddf.minim.Minim
import ddf.minim.ugens.*
import ddf.minim.ugens.Waves.SINE
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


var minim: Minim? = null
var out: AudioOutput? = null
//var wave: Oscil? = null
//var re: Oscil? = null
class Oscillator(freq:Float,amp:Float,waveForm:Wavetable,lfoFre: Float,lfoAmp: Float) {
    var re = Oscil(lfoFre,lfoAmp,SINE)
    //var adsr = ADSR(1.0f,0.1f,0.1f,0.1f,0.1f)
    var adsr = ADSR(1.0f,0.1f,0.1f,0.1f,0.01f)
    var wave = Oscil(freq, 0.60f, waveForm)
    var wavea = Oscil(freq, 0.60f, Waves.triangleh(3))



    init {
        val minim = Minim(object : kotlin.Any() {
            fun sketchPath(fileName: String): String {
                return fileName
            }

            fun createInput(fileName: String): InputStream {
                return FileInputStream(File(fileName))
            }
        })
        re.offset.setLastValue(200.0f)
        //re.patch(wavep.frequency)
        wave.patch(adsr)
        wavea.patch(adsr)
        //var disWave: Waveform = Waves.sawh( 4 )
        //il va falloir créer un nombre de wave variées qui sont assignables à chaque lettre ahah
       // disWave = Waves.add(floatArrayOf(0.5f, 0.5f), Waves.sawh(4), Waves.squareh(3))
    }

    fun updateWave(lfoFre:Float,lfoAmp:Float,fre:Float,form:Wavetable){
        re.setAmplitude(lfoAmp)
        re.setFrequency(lfoFre)
        //re.patch(wave.frequency)
        //wave.setFrequency(fre-60.0f)
        wave.setFrequency(fre)
        wavea.setFrequency(fre)
        wave.waveform = form
        //wave.patch(adsr)
        //println(lfoFre.toString()+" + "+fre.toString()+" + "+lfoAmp.toString()+" + "+form.toString())
    }
}
