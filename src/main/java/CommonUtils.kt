import java.util.*

class CommonUtils {

        fun randomPhoneGenerator(): String? {

            val generator = Random()

            val num1 = generator.nextInt(7) + 1 //add 1 so there is no 0 to begin
            val num2 = generator.nextInt(8)
            val num3 = generator.nextInt(8)

            val set2 = generator.nextInt(643) + 100
            val set3 = generator.nextInt(8999) + 1000

            return ("+1$num1$num2$num3$set2$set3")
        }

        fun randomEmailGenerator(): String? {
            val generator = Random()
            val word = "abcdefghijklmnopqrstuvwxyz";
            var mail = "";
            for (i in 0..5){
                var index = generator.nextInt(word.length - 1);
                mail += word.substring(index, index + 1)
            }

            return mail + "_autotest@gmail.com";
        }
}