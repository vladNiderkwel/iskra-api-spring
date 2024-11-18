package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.*
import com.niderkvel.iskraapispring.forms.LoginForm
import com.niderkvel.iskraapispring.forms.NewStaffForm
import com.niderkvel.iskraapispring.forms.PageResponse
import com.niderkvel.iskraapispring.forms.StaffForm
import com.niderkvel.iskraapispring.models.Staff
import com.niderkvel.iskraapispring.repositories.StaffRepository
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/staff")
class StaffController {

    @Autowired
    lateinit var staffRepo: StaffRepository

    @GetMapping(path = ["", "/"])
    fun getAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "") query: String
    ): PageResponse<List<Staff>> {
        val content = staffRepo.findAll(
            "%$query%",
            Pageable.ofSize(ELEMENTS_ON_PAGE).withPage(page - 1)
        )

        return PageResponse(
            content = content.content.toList(),
            totalPages = content.totalPages,
            currentPage = page
        )
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Int
    ): Staff? =
        staffRepo.findByIdOrNull(id)

    @PostMapping(
        path = ["", "/"]
    )
    fun save(
        @RequestBody staffForm: StaffForm
    ): Int {
        return staffRepo
            .save(
                Staff(
                    name = staffForm.name,
                    email = staffForm.email,
                    password = encodePassword(staffForm.password),
                    role = staffForm.role
                )
            )
            .id
    }

    @PutMapping(
        path = ["", "/"]
    )
    fun update(
        @RequestBody staff: Staff
    ) =
        staffRepo.save(staff)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Int) =
        staffRepo.deleteById(id)

    @PostMapping("/login")
    fun login(
        session: HttpSession,
        @RequestBody form: LoginForm
    ): ResponseEntity<Staff?> {
        val staff = staffRepo.findByEmail(form.email) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)

        if (matchPasswords(form.password, staff.password))
            return ResponseEntity(staff, HttpStatus.OK)

        return ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @GetMapping("/block/{id}")
    fun toggleBlock(
        @PathVariable id: Int
    ): Boolean {
        val staff = staffRepo.findByIdOrNull(id) ?: return false

        staff.isBlocked = !staff.isBlocked

        return staffRepo.save(staff).isBlocked
    }

    @PostMapping("/new")
    fun newStaff(
        @RequestBody staffForm: StaffForm
    ): ResponseEntity<Boolean> {

        kotlin.runCatching {
            staffRepo
                .save(
                    Staff(
                        name = staffForm.name,
                        email = staffForm.email,
                        password = encodePassword(staffForm.password),
                        role = staffForm.role,
                        isPasswordChanged = false
                    )
                )
        }
            .onFailure {
                return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .onSuccess {
                return ResponseEntity(true, HttpStatus.OK)
            }

        return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @PostMapping("/signup")
    fun signup(
        @RequestBody newStaffForm: NewStaffForm
    ): ResponseEntity<Staff?> {

        val staff = staffRepo.findByEmail(newStaffForm.email) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)

        if (staff.isPasswordChanged)
            return ResponseEntity(null, HttpStatus.NOT_FOUND)

        if (!matchPasswords(newStaffForm.oldPassword, staff.password))
            return ResponseEntity(null, HttpStatus.NOT_FOUND)

        val response = ResponseEntity<Staff?>(
            staffRepo
                .save(
                    Staff(
                        id = staff.id,
                        name = staff.name,
                        email = staff.email,
                        password = encodePassword(newStaffForm.newPassword),
                        role = staff.role,
                        isPasswordChanged = true
                    )
                ),
            HttpStatus.OK
        )

        return response
    }
}