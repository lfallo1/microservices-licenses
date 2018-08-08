package com.thoughtmechanix.licenses.repositories;

import com.thoughtmechanix.licenses.model.License;
import org.springframework.data.repository.CrudRepository;

public interface LicensesRepository extends CrudRepository<License, String> {
}
