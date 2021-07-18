import ddf.minim.AudioOutput
import ddf.minim.Minim
import ddf.minim.*
import ddf.minim.ugens.*
import ddf.minim.ugens.Waves.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


var minim: Minim? = null
var out: AudioOutput? = null
var wave: Oscil? = null
class Oscillator {
    init {
        val minim = Minim(object : kotlin.Any() {
            fun sketchPath(fileName: String): String {
                return fileName
            }

            fun createInput(fileName: String): InputStream {
                return FileInputStream(File(fileName))
            }
        })
        var out = minim.lineOut
        var wave = Oscil(440.0f, 0.5f, SINE)
        wave.patch(out)


    }
}
