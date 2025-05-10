Feature: Read Company API
  As an {actor}
  I want to perform actions on the Read Company API
  So that I can retrieve company details based on different criteria

  Background:
    Given the API is available
    And the following companies exist in the system:
      | companyId                             | rsin      | inp_id    | name              | email                  | address              | status   |
      | 111e4567-e89b-12d3-a456-426614174100 | 123456789 | INP123456 | Acme Corp         | info@acme.com          | Main St 1, Amsterdam | ACTIVE   |
      | 211e4567-e89b-12d3-a456-426614174101 | 987654321 | INP654321 | Beta BV           | contact@beta-bv.nl     | Betaweg 2, Utrecht   | INACTIVE |
      | 311e4567-e89b-12d3-a456-426614174102 | 112233445 | null      | Gamma NV          | office@gammanv.be       | Gamma 3, Brussels    | ACTIVE   |
      | 411e4567-e89b-12d3-a456-426614174103 | null      | INP111222 | Delta LLC         | mail@deltallc.com      | Delta 4, Rotterdam   | ACTIVE   |
      | 511e4567-e89b-12d3-a456-426614174104 | 556677889 | null      | Epsilon GmbH      | info@epsilon-gmbh.de   | Epsilon 5, Berlin    | ACTIVE   |
      | 611e4567-e89b-12d3-a456-426614174105 | null      | INP333444 | Zeta SARL         | hello@zetasarl.fr      | Zeta 6, Paris        | INACTIVE |
      | 711e4567-e89b-12d3-a456-426614174106 | 334455667 | null      | Eta Ltd           | eta@etaltd.co.uk       | Eta 7, London        | ACTIVE   |
      | 811e4567-e89b-12d3-a456-426614174107 | 778899001 | INP555666 | Theta SpA         | info@thetaspait        | Theta 8, Rome        | ACTIVE   |

  Scenario: Retrieve a company by ID as a normal user
    Given the user is authenticated with normal access rights
    When the user reads a company with ID "111e4567-e89b-12d3-a456-426614174100"
    Then the user receives status code 200
    And the user receives a company with the following details:
      | companyId  | 111e4567-e89b-12d3-a456-426614174100 |
      | rsin       | 123456789                            |
      | name       | Acme Corp                            |
      | email      | info@acme.com                        |

  Scenario: Retrieve a company by RSIN as a normal user
    Given the user is authenticated with normal access rights
    When the user reads a company with RSIN "123456789"
    Then the user receives status code 200
    And the user receives a company with the following details:
      | companyId  | 111e4567-e89b-12d3-a456-426614174100 |
      | rsin       | 123456789                            |
      | name       | Acme Corp                            |

  Scenario: Retrieve a company by INP_ID as a normal user
    Given the user is authenticated with normal access rights
    When the user reads a company with INP_ID "INP123456"
    Then the user receives status code 200
    And the user receives a company with the following details:
      | companyId  | 111e4567-e89b-12d3-a456-426614174100 |
      | inp_id     | INP123456                            |
      | name       | Acme Corp                            |

  Scenario: Retrieve a company by ID as an admin user
    Given the user is authenticated with admin access rights
    When the user reads a company with ID "111e4567-e89b-12d3-a456-426614174100"
    Then the user receives status code 200
    And the user receives a company with the following details:
      | companyId  | 111e4567-e89b-12d3-a456-426614174100 |
      | rsin       | 123456789                            |
      | name       | Acme Corp                            |
      | email      | info@acme.com                        |

  Scenario: Retrieve a company by RSIN as an admin user
    Given the user is authenticated with admin access rights
    When the user reads a company with RSIN "123456789"
    Then the user receives status code 200
    And the user receives a company with the following details:
      | companyId  | 111e4567-e89b-12d3-a456-426614174100 |
      | rsin       | 123456789                            |
      | name       | Acme Corp                            |

  Scenario: Retrieve a company by INP_ID as an admin user
    Given the user is authenticated with admin access rights
    When the user reads a company with INP_ID "INP123456"
    Then the user receives status code 200
    And the user receives a company with the following details:
      | companyId  | 111e4567-e89b-12d3-a456-426614174100 |
      | inp_id     | INP123456                            |
      | name       | Acme Corp                            |

  # Negative test scenarios
  Scenario: Attempt to retrieve company with non-existent ID
    Given the user is authenticated with normal access rights
    When the user reads a company with ID "999e4567-e89b-12d3-a456-426614174999"
    Then the user receives status code 404
    And the user receives an error message "Company not found"

  Scenario: Attempt to retrieve company with non-existent RSIN
    Given the user is authenticated with normal access rights
    When the user reads a company with RSIN "000000000"
    Then the user receives status code 404
    And the user receives an error message "Company not found"

  Scenario: Attempt to retrieve company with non-existent INP_ID
    Given the user is authenticated with normal access rights
    When the user reads a company with INP_ID "INP999888"
    Then the user receives status code 404
    And the user receives an error message "Company not found"

  Scenario: Attempt to retrieve company without providing any identifier
    Given the user is authenticated with normal access rights
    When the user reads a company without providing RSIN or INP_ID
    Then the user receives status code 400
    And the user receives an error message "At least one search parameter is required"

  Scenario: Attempt to access company with insufficient permissions
    Given the user is authenticated with user access rights
    When the user reads a company with ID "311e4567-e89b-12d3-a456-426614174102"
    Then the user receives status code 403
    And the user receives an error message "Insufficient permissions to access this company's details"

  # RSIN validation scenarios
  Scenario: Attempt to retrieve company with RSIN of invalid length
    Given the user is authenticated with normal access rights
    When the user reads a company with RSIN "1234"
    Then the user receives status code 400
    And the user receives an error message "Invalid RSIN length"

  Scenario: Attempt to retrieve company with RSIN containing letters
    Given the user is authenticated with normal access rights
    When the user reads a company with RSIN "12AB56789"
    Then the user receives status code 400
    And the user receives an error message "Invalid RSIN format"

  Scenario: Successfully retrieve company with valid RSIN
    Given the user is authenticated with normal access rights
    When the user reads a company with RSIN "778899001"
    Then the user receives status code 200
    And the user receives a company with the following details:
      | companyId  | 811e4567-e89b-12d3-a456-426614174107 |
      | rsin       | 778899001                            |
      | name       | Theta SpA                            |

  # Corner cases - RSIN/INP_ID exclusivity
  Scenario: Attempt to read a company with both RSIN and INP_ID
    Given the user is authenticated with normal access rights
    When the user reads a company with RSIN "123456789" and INP_ID "INP123456"
    Then the user receives status code 400
    And the user receives an error message "Both RSIN and INP_ID provided; only one should be provided"

  Scenario: Retrieve company with missing RSIN but valid INP_ID
    Given the user is authenticated with normal access rights
    When the user reads a company with INP_ID "INP333444"
    Then the user receives status code 200
    And the user receives a company with the following details:
      | companyId  | 611e4567-e89b-12d3-a456-426614174105 |
      | inp_id     | INP333444                            |
      | name       | Zeta SARL                            |

  Scenario: Retrieve company with missing INP_ID but valid RSIN
    Given the user is authenticated with normal access rights
    When the user reads a company with RSIN "123456789"
    Then the user receives status code 200
    And the user receives a company with the following details:
      | companyId  | 111e4567-e89b-12d3-a456-426614174100 |
      | rsin       | 123456789                            |
      | inp_id     | INP123456                            |
      | name       | Acme Corp                            |

  # Email validation scenarios
  Scenario: Attempt to retrieve a company with invalid email format
    Given the user is authenticated with normal access rights
    When the user creates a company with email "invalid-email@"
    Then the user receives status code 400
    And the user receives an error message "Invalid email format"

  Scenario: Attempt to retrieve a company with email containing illegal characters
    Given the user is authenticated with normal access rights
    When the user creates a company with email "test#$%^@acme.com"
    Then the user receives status code 400
    And the user receives an error message "Email contains illegal characters"

  # Additional corner cases
  Scenario: Retrieve company with invalid format RSIN
    Given the user is authenticated with normal access rights
    When the user reads a company with RSIN "12-3456789"
    Then the user receives status code 400
    And the user receives an error message "Invalid RSIN format"

  Scenario: Retrieve company with invalid format ID
    Given the user is authenticated with normal access rights
    When the user reads a company with ID "not-a-valid-uuid"
    Then the user receives status code 400
    And the user receives an error message "Invalid UUID format"

  # Authentication scenarios
  Scenario: Attempt to retrieve company without authentication
    When the user reads a company with ID "111e4567-e89b-12d3-a456-426614174100" without authentication
    Then the user receives status code 401
    And the user receives an error message "Authentication required"

  Scenario: Attempt to retrieve company with expired authentication token
    Given the user has an expired authentication token
    When the user reads a company with ID "111e4567-e89b-12d3-a456-426614174100"
    Then the user receives status code 401
    And the user receives an error message "Authentication token expired"

  Scenario: Attempt to retrieve company with invalid authentication token
    Given the user has an invalid authentication token
    When the user reads a company with ID "111e4567-e89b-12d3-a456-426614174100"
    Then the user receives status code 401
    And the user receives an error message "Invalid authentication token"

  # Invalid UUID format scenarios  
  Scenario: Attempt to retrieve company with malformed UUID
    Given the user is authenticated with normal access rights
    When the user reads a company with ID "not-a-valid-uuid"
    Then the user receives status code 400
    And the user receives an error message "Invalid UUID format"

  # Performance scenarios
  Scenario: Verify response time for company retrieval
    Given the user is authenticated with normal access rights
    When the user reads a company with ID "111e4567-e89b-12d3-a456-426614174100"
    Then the user receives status code 200
    And the response time should be less than 500 milliseconds

  # Security scenarios
  Scenario: Attempt SQL injection via ID parameter
    Given the user is authenticated with normal access rights
    When the user reads a company with ID "' OR 1=1; --"
    Then the user receives status code 400
    And the user receives an error message "Invalid UUID format"
    And the system should log the attempted injection

  # API behavior scenarios
  Scenario: Verify API returns content-type application/json
    Given the user is authenticated with normal access rights
    When the user reads a company with ID "111e4567-e89b-12d3-a456-426614174100"
    Then the user receives status code 200
    And the response content type should be "application/json"

  Scenario: API remains responsive under load
    Given the user is authenticated with normal access rights
    When the user sends 50 concurrent requests to read different companies
    Then all responses should be received within 2 seconds
    And at least 95% of responses should have status code 200
