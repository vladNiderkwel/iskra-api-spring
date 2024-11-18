package com.niderkvel.iskraapispring.interceptors

import com.niderkvel.iskraapispring.ServiceConfig
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor

class TaskInterceptor : HandlerInterceptor {

    @Autowired
    lateinit var services: ServiceConfig

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {

        if (!services.tasks) {
            response.status = HttpStatus.SERVICE_UNAVAILABLE.value()
            return false
        }

        return true
    }
}