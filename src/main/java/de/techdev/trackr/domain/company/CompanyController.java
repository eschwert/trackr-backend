package de.techdev.trackr.domain.company;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Moritz Schulze
 */
@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/createWithAddress", method = {RequestMethod.POST}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Company createWithAddress(@RequestBody @Valid CreateCompany createCompany, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RepositoryConstraintViolationException(bindingResult);
        }
        createCompany.getCompany().setAddress(createCompany.getAddress());
        return companyRepository.saveAndFlush(createCompany.getCompany());
    }

    /**
     * DTO for creating a company with an address.
     * <p>
     * This class <b>must</b> be static, otherwise the binding errors will not work correctly.
     */
    @Data
    protected static class CreateCompany {
        @Valid
        private Company company;

        @Valid
        private Address address;
    }
}
