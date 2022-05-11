package com.example.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    val helper = SqliteHelper(this, "memo", 1)//SqliteHelper 생성
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = RecyclerAdapter()
        adapter.helper = helper//helper를 어댑터에 연결
        adapter.listData.addAll(helper.selectMemo())//어댑터의 listData에 디비에서 가져온 데이터 세팅

        binding.recyclerMemo.adapter = adapter//어댑터 연결
        binding.recyclerMemo.layoutManager = LinearLayoutManager(this)//레이아웃 매니지 설정

        binding.buttonSave.setOnClickListener {
            if(binding.editMemo.text.toString().isNotEmpty()){//플레인 텍스트에 값이 있으면
                val memo = Memo(null, binding.editMemo.text.toString(), System.currentTimeMillis())
                helper.insertMemo(memo)//생성한 메모를 데이터베이스에 저장
                adapter.listData.clear()//어댑터의 데이터 모두 초기화

                adapter.listData.addAll(helper.selectMemo())//데이터베이스에서 새로운 목록을 읽어와 세팅하고 갱신
                adapter.notifyDataSetChanged()

                binding.editMemo.setText("")
            }
        }
    }
}