package pk.inlab.team.app.mem.utils

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun convertLongToDateMonthYear(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("EEE dd-MM-yyyy", Locale.US)
            return format.format(date)
        }

        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("hh:mm a", Locale.US)
            return format.format(date)
        }

    }
}