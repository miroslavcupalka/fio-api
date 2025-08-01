package cz.gpt.fioexample

import cz.gpt.fioapi.FioApiImpl
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val token = "token"
    val fio = FioApiImpl()
    val response = fio.getLastTransactions(token)

    println("Info o účtu: ${'$'}{response.accountStatement.info}")
    println("Transakce:")
    response.accountStatement.transactionList?.transaction?.forEach {
        println("Datum: ${'$'}{it.column0?.value} | Částka: ${'$'}{it.column1?.value} | Typ: ${'$'}{it.column8?.value}")
    }
}