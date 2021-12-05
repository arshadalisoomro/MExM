package pk.inlab.team.app.mem.ui.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import pk.inlab.team.app.mem.R

class InputDialogFragment(private val title: String) : DialogFragment() {

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout to use as dialog or embedded fragment
//        val view = inflater.inflate(R.layout.purchase_item, container, false)
//        dialog!!.window!!.decorView
//        dialog?.setTitle(title)
//
//        return view
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val decorView = dialog!!.window!!.decorView
//        activity?.let {
//            // Build the dialog and set up the button click handlers
//            val builder = AlertDialog.Builder(it)
//
//            builder.setCancelable(false)
//                .setPositiveButton(R.string.save,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // Send the positive button event back to the host activity
//                        listener.onDialogPositiveClick(this)
//                    })
//                .setNegativeButton(R.string.cancel,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // Send the negative button event back to the host activity
//                        listener.onDialogNegativeClick(this)
//                    })
//
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        val inflater = LayoutInflater.from(context)
        // Inflate the layout to use as dialog or embedded fragment
        val view = inflater.inflate(R.layout.input_purchase_item, null, false)

        // Build the dialog and set up the button click handlers
        var builder: AlertDialog.Builder? = null
        activity?.let {
            // Build the dialog and set up the button click handlers
            builder = AlertDialog.Builder(it)
            builder!!.setTitle(title)
            builder!!.setView(view)
            builder!!.setCancelable(false)
                .setPositiveButton(R.string.save
                ) { /*dialog*/ _, /*id*/ _ ->
                    // Send the positive button event back to the host activity
                    listener.onDialogPositiveClick(this)
                }
                .setNegativeButton(R.string.cancel
                ) { /*dialog*/ _, /*id*/ _ ->
                    // Send the negative button event back to the host activity
                    listener.onDialogNegativeClick(this)
                }

            builder!!.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        return builder?.create()!!
    }

    // Use this instance of the interface to deliver action events
    private lateinit var listener: InputDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface InputDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as InputDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }
}