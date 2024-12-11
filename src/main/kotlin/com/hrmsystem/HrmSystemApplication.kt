package com.hrmsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HrmSystemApplication

fun main(args: Array<String>) {
    runApplication<HrmSystemApplication>(*args)
}