package pk.inlab.team.app.mem.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {

        private const val month = 0
        private const val year = 0

        private var calendar: Calendar = Calendar.getInstance()

        var locale = Locale("en", "US")

        init {
            calendar.set(year, month, 1)
        }

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

        private fun min(): String {
            return java.lang.String.valueOf(
                Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH)
            )
        }

        private fun max(): String {
            return java.lang.String.valueOf(
                Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
            )
        }

        fun startOfCurrentMonth(): Int {
            return Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH)
        }

        fun endOfCurrentMonth(): Int {
            return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        fun getToday(): String? {
            return SimpleDateFormat("MMM", locale)
                .format(Calendar.getInstance().time).uppercase(locale) +
                    " " + min() + "-" + max() +
                    ", " + Calendar.getInstance()[Calendar.YEAR]
        }

        fun getDate(date: Date): String? {
            return SimpleDateFormat("dd", locale)
                .format(date).toString() +
                    " " +
                    SimpleDateFormat("hh:mm a", locale)
                        .format(date)
        }

        fun getMonthDay(): String? {
            val result: String
            val date = Calendar.getInstance()[Calendar.DATE]
            result = if (date > 9) {
                date.toString()
            } else {
                "0$date"
            }
            return SimpleDateFormat("MMM", locale)
                .format(Calendar.getInstance().time).uppercase(locale) +
                    "," + result
        }

        fun getMonthDay(dateCal: Calendar): String? {
            val result: String
            val date = dateCal[Calendar.DATE]
            result = if (date > 9) {
                date.toString()
            } else {
                "0$date"
            }
            return SimpleDateFormat("MMM", locale)
                .format(dateCal.time).uppercase(locale) +
                    "," + result
        }
    }
}