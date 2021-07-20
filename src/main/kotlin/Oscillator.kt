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
class Oscillator(freq:Float,amp:Float,waveForm:Int) {
    var waveName = SINE
    var re = Oscil(10.0f,2.0f,SINE)
    var wave = Oscil(freq, amp, waveName)

    init {
        val minim = Minim(object : kotlin.Any() {
            fun sketchPath(fileName: String): String {
                return fileName
            }

            fun createInput(fileName: String): InputStream {
                return FileInputStream(File(fileName))
            }
        })
        if(waveForm==0){
            waveName = SINE
        }
        else if(waveForm==1){
            waveName = TRIANGLE
        }
        else if(waveForm==2){
            waveName = SAW
        }
        else if(waveForm==3){
            waveName = SQUARE
        }
        wave.setWaveform(waveName)
        re.offset.setLastValue(200.0f)
        re.patch(wave)

    }

    fun updateWave(lfoFre:Float,lfoAmp:Float,fre:Float,form:Wavetable){
        re.setAmplitude(lfoAmp)
        re.setFrequency(lfoFre)
        wave.setFrequency(fre)
        wave.setWaveform(form)
    }
}
