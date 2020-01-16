class ApproveSnackbar(parent: ViewGroup, content: View, contentViewCallback: ContentViewCallback) : BaseTransientBottomBar<ApproveSnackbar>(parent, content, contentViewCallback){

    fun setText(message: CharSequence): ApproveSnackbar {
        val tvMessage = getView().findViewById<TextView>(R.id.tv_message)

        tvMessage.text = message

        return this
    }

    fun setApproveAction(listener: View.OnClickListener): ApproveSnackbar {
        val btnAction = getView().findViewById<Button>(R.id.btn_approve)

        btnAction.visibility = View.VISIBLE
        btnAction.setOnClickListener{view -> listener.onClick(view)}

        return this
    }

    fun setRejectAction(listener: View.OnClickListener): ApproveSnackbar {
        val btnAction = getView().findViewById<Button>(R.id.btn_reject)

        btnAction.visibility = View.VISIBLE
        btnAction.setOnClickListener { view -> listener.onClick(view)}

        return this
    }

    fun setMessage(message: CharSequence){
        val tvMessage = getView().findViewById<TextView>(R.id.tv_message)

        tvMessage.text = message
    }

    class ContentViewCallback(private val view: View): BaseTransientBottomBar.ContentViewCallback {
        override fun animateContentIn(delay: Int, duration: Int){
        }

        override fun animateContentOut(delay: Int, duration: Int){
        }
    }

    companion object {
        fun make(parent: ViewGroup, @Duration duration: Int): ApproveSnackbar{
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.snackbar_approve, parent, false)
            val callback = ContentViewCallback(view)

            val twoButtonSnackbar = ApproveSnackbar(parent, view, callback)
            twoButtonSnackbar.getView().setPadding(0, 0, 0, 0)

            twoButtonSnackbar.duration = duration

            return twoButtonSnackbar
        }
    }
}

