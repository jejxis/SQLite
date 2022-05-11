package com.example.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(context: Context, name: String, version: Int): SQLiteOpenHelper(context, name, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val create = "create table memo ("+
                "no integer primary key, "+
                "content text, "+
                "datetime integer"+
                ")"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertMemo(memo: Memo){
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        val wd = writableDatabase
        wd.insert("memo", null, values)//테이블명(memo)와 insert할 값(values)
        wd.close()
    }

    fun selectMemo(): MutableList<Memo>{//데이터 조회
        val list = mutableListOf<Memo>()

        val select = "select * from memo"//전체 데이터 조회 쿼리
        val rd = readableDatabase//읽기 전용 데이터베이스
        val cursor = rd.rawQuery(select, null)//쿼리 담기.커서: 데이터셋 처리 시 현재 위치 포함하는 데이터 요소. 책갈피랑 비슷.

        while(cursor.moveToNext()){//moveToNext: 다음 줄에 사용할 수 있는 레코드가 있으면 true 반환
            val noIdx = cursor.getColumnIndex("no")//테이블에서 no 컬럼이 몇 번째 컬럼인지 알아낸다.
            val contentIdx = cursor.getColumnIndex("content")//content column
            val dateIdx = cursor.getColumnIndex("datetime")//datetime column

            val no = cursor.getLong(noIdx)//위에서 저장해 둔 컬럼의 위치에서 값을 가져온다.
            val content = cursor.getString(contentIdx)
            val datetime = cursor.getLong(dateIdx)

            list.add(Memo(no, content, datetime))
        }
        cursor.close()//커서 닫기
        rd.close()//읽기 전용 데이터베이스 닫기

        return list
    }

    fun updateMemo(memo: Memo){//데이터 수정
        val values = ContentValues()
        values.put("content", memo.content)//수정할 값들 저장하기
        values.put("datetime", memo.datetime)

        val wd = writableDatabase
        wd.update("memo", values, "no = ${memo.no}", null)//테이블명, 수정할 값, 수정할 조건
        wd.close()
    }

    fun deleteMemo(memo: Memo){
        val delete = "delete from memo where no = ${memo.no}"

        val db = writableDatabase
        db.execSQL(delete)
        db.close()
    }
}
data class Memo(var no: Long?, var content: String, var datetime: Long)