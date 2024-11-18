package com.niderkvel.iskraapispring.interceptors
/*
import com.niderkvel.iskraapispring.SESSION_ATTRIBUTE_CURRENT_USER
import com.niderkvel.iskraapispring.SESSION_ATTRIBUTE_STAFF
import com.niderkvel.iskraapispring.SESSION_ATTRIBUTE_USER
import com.niderkvel.iskraapispring.SESSION_ATTRIBUTE_USER_ROLE
import com.niderkvel.iskraapispring.models.Staff
import com.niderkvel.iskraapispring.models.User
import com.niderkvel.iskraapispring.repositories.StaffRepository
import com.niderkvel.iskraapispring.repositories.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor

class TokenInterceptor : HandlerInterceptor {

    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var staffRepo: StaffRepository

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val token = request.getHeader("AUTHORIZATION")

        if (token == null) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            return false
        }

        val obj = request.session.getAttribute(SESSION_ATTRIBUTE_CURRENT_USER) ?: return false

        request.session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE)?.let {

            val who = it as String

            when (who) {
                SESSION_ATTRIBUTE_USER ->
                    if ((obj as User).token == token) return true

                SESSION_ATTRIBUTE_STAFF ->
                    if ((obj as Staff).token == token) return true

                else -> {
                    response.status = HttpStatus.UNAUTHORIZED.value()
                    return false
                }
            }
        }

        response.status = HttpStatus.UNAUTHORIZED.value()
        return false
    }
}
*/