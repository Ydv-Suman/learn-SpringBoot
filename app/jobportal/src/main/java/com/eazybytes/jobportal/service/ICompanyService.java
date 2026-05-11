package com.eazybytes.jobportal.service;

import com.eazybytes.jobportal.dto.CompanyDto;

import java.util.List;

public interface ICompanyService {

    List<CompanyDto> getAllCompanies();

}