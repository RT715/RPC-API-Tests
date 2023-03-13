import java.util.*
import kotlin.math.pow

object Utilities {

    fun randomValue(exp: Int): Int = Random().nextInt(10.0.pow(exp).toInt())
}