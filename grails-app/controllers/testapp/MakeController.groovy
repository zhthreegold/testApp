package testapp

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class MakeController {

    MakeService makeService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond makeService.list(params), model:[makeCount: makeService.count()]
    }

    def show(Long id) {
        respond makeService.get(id)
    }

    def create() {
        respond new Make(params)
    }

    def save(Make make) {
        if (make == null) {
            notFound()
            return
        }

        try {
            makeService.save(make)
        } catch (ValidationException e) {
            respond make.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'make.label', default: 'Make'), make.id])
                redirect make
            }
            '*' { respond make, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond makeService.get(id)
    }

    def update(Make make) {
        if (make == null) {
            notFound()
            return
        }

        try {
            makeService.save(make)
        } catch (ValidationException e) {
            respond make.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'make.label', default: 'Make'), make.id])
                redirect make
            }
            '*'{ respond make, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        makeService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'make.label', default: 'Make'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'make.label', default: 'Make'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
