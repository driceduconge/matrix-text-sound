import ddf.minim.AudioOutput
import ddf.minim.Minim
//import ddf.minim.*
import ddf.minim.ugens.*
import ddf.minim.ugens.Waves.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


var minim: Minim? = null
var out: AudioOutput? = null
//var wave: Oscil? = null
//var re: Oscil? = null
class Oscillator(freq:Float,amp:Float,waveForm:Wavetable,lfoFre: Float,lfoAmp: Float) {
    var re = Oscil(lfoFre,lfoAmp,SINE)
    var adsr = ADSR(1.0f,0.1f,0.4f,0.5f,0.3f)
    var wave = Oscil(freq, amp, waveForm)

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
        re.patch(wave)
        wave.patch(adsr)

    }

    fun updateWave(lfoFre:Float,lfoAmp:Float,fre:Float,form:Wavetable){
        re.setAmplitude(lfoAmp)
        re.setFrequency(lfoFre)
        //re.patch(wave)
        wave.setFrequency(fre)
        wave.waveform = form
        //println(lfoFre.toString()+" + "+fre.toString()+" + "+lfoAmp.toString()+" + "+form.toString())
    }
}
