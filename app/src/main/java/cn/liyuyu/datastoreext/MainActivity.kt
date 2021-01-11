package cn.liyuyu.datastoreext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import cn.liyuyu.datastoreext.core.DsPreferences
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch(Dispatchers.IO) {
            var foo = Foo("bar", 18)
            DsPreferences.set("foo", foo)
            val value = DsPreferences.get<Foo>("foo")
            withContext(Dispatchers.Main) {
                findViewById<TextView>(R.id.tvValue).text = value.toString()
            }
        }

        viewModel.appSettings.observe(this, {
            findViewById<TextView>(R.id.tvValue).text = it?.toString()
        })
    }

    fun onSaveData(view: View) {
        val text = findViewById<EditText>(R.id.editText).text.toString()
        if (text.isNotEmpty()) {
            launch(Dispatchers.IO) {
                DsPreferences.set("settings", text)
            }
        }
    }
}