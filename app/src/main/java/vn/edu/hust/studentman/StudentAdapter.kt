package vn.edu.hust.studentman

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(val students: MutableList<StudentModel>, val context: Context) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
        val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
        val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
        val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_student_item,
            parent, false
        )
        return StudentViewHolder(itemView)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]

        holder.textStudentName.text = student.studentName
        holder.textStudentId.text = student.studentId

        holder.imageEdit.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.layout_add_new_dialog)
            val editName = dialog.findViewById<EditText>(R.id.edit_name)
            val editId = dialog.findViewById<EditText>(R.id.edit_id)
            val textView = dialog.findViewById<TextView>(R.id.textview)
            textView.setText("Student information:")
            editName.setText(student.studentName)
            editId.setText(student.studentId)
            dialog.findViewById<Button>(R.id.button_ok).setOnClickListener {
                val newName = editName.text.toString()
                val newId = editId.text.toString()
                student.studentName = newName
                student.studentId = newId
                notifyItemChanged(position)
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.button_cancel).setOnClickListener {
                dialog.dismiss()
            }
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.show()
        }

        holder.imageRemove.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Student")
            builder.setMessage("Are you sure you want to delete this student?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                val removedStudent = students.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, students.size)
                dialog.dismiss()

                Snackbar.make(holder.itemView, "Student deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        students.add(position, removedStudent)
                        notifyItemInserted(position)
                    }.show()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()

        }
    }
}