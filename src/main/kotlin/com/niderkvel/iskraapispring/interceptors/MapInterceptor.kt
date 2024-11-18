package com.niderkvel.iskraapispring.interceptors

import com.niderkvel.iskraapispring.ServiceConfig
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor

class MapInterceptor : HandlerInterceptor {

    @Autowired
    lateinit var serviceConfig: ServiceConfig

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {

        if (!serviceConfig.map) {
            response.status = HttpStatus.SERVICE_UNAVAILABLE.value()
            return false
        }

        return true
    }
}