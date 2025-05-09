Feature: Read Person API
  As an {actor}
  I want to perform actions on the Read Person API
  So that I can retrieve person details based on different criteria

  Background:
    Given the API is available
    And the following persons exist in the system:
      | id                                   | source | bsn         | anp_id     | last_name | last_name_prefix | initials | gender_designation | date_of_birth | authorized_person_id                 | email                | indication_secret |
      | 123e4567-e89b-12d3-a456-426614174000 | BRP    | 123456789   | null       | Johnson   |                  | A.B.    | M                 | 19800101      | null                                 | ajohnson@example.com | 0                |
      | 223e4567-e89b-12d3-a456-426614174001 | ANP    | null        | ANP123456  | Smith     |                  | C.D.    | F                 | 19850215      | null                                 | csmith@example.com   | 0                |
      | 323e4567-e89b-12d3-a456-426614174002 | BRP    | 987654321   | null       | Brown     | van der          | E.F.    | M                 | 19900530      | 723e4567-e89b-12d3-a456-426614174006 | ebrown@example.com   | 1                |
      | 423e4567-e89b-12d3-a456-426614174003 | ANP    | null        | null       | Miller    | de               | G.H.    | F                 | 19750420      | null                                 | gmiller@example.com  | 0                |
      | 523e4567-e89b-12d3-a456-426614174004 | BRP    | 111222333   | null       | Davis     |                  | I.J.    | X                 | 19950610      | null                                 | idavis@example.com   | 0                |
      | 623e4567-e89b-12d3-a456-426614174005 | ANP    | null        | ANP111222  | Wilson    |                  | K.L.    | M                 | null          | 723e4567-e89b-12d3-a456-426614174006 | kwilson@example.com  | 1                |
      | 723e4567-e89b-12d3-a456-426614174006 | BRP    | 444555666   | null       | Anderson  |                  | M.N.    | F                 | 19780305      | null                                 | manderson@example.com| 0                |
      | 823e4567-e89b-12d3-a456-426614174007 | BRP    | 619631941   | null       | Williams  |                  | O.P.    | M                 | 19920712      | null                                 | owilliams@example.com| 0                |

  Scenario: Retrieve a person by ID as a normal user
    Given the user is authenticated with normal access rights
    When the user reads a person with ID "123e4567-e89b-12d3-a456-426614174000"
    Then the user receives status code 200
    And the user receives a person with the following details:
      | id         | 123e4567-e89b-12d3-a456-426614174000 |
      | source     | BRP                                  |
      | bsn        | 123456789                            |
      | lastName   | Johnson                              |
      | initials   | A.B.                                 |
      | email      | ajohnson@example.com                 |

  Scenario: Retrieve a person by BSN as a normal user
    Given the user is authenticated with normal access rights
    When the user reads a person with BSN "123456789"
    Then the user receives status code 200
    And the user receives a person with the following details:
      | id         | 123e4567-e89b-12d3-a456-426614174000 |
      | source     | BRP                                  |
      | bsn        | 123456789                            |
      | anpId      | null                                 |
      | lastName   | Johnson                              |

  Scenario: Retrieve a person by ANP_ID as a normal user
    Given the user is authenticated with normal access rights
    When the user reads a person with ANP_ID "ANP123456"
    Then the user receives status code 200
    And the user receives a person with the following details:
      | id         | 223e4567-e89b-12d3-a456-426614174001 |
      | source     | ANP                                  |
      | bsn        | null                                 |
      | anpId      | ANP123456                            |
      | lastName   | Smith                                |

  Scenario: Retrieve a person by ID as an admin user
    Given the user is authenticated with normal access rights
    When the user reads a person with ID "123e4567-e89b-12d3-a456-426614174000"
    Then the user receives status code 200
    And the user receives a person with the following details:
      | id         | 123e4567-e89b-12d3-a456-426614174000 |
      | source     | BRP                                  |
      | bsn        | 123456789                            |
      | lastName   | Johnson                              |
      | initials   | A.B.                                 |
      | email      | ajohnson@example.com                 |

  Scenario: Retrieve a person by BSN as an admin user
    Given the user is authenticated with normal access rights
    When the user reads a person with BSN "123456789"
    Then the user receives status code 200
    And the user receives a person with the following details:
      | id         | 123e4567-e89b-12d3-a456-426614174000 |
      | source     | BRP                                  |
      | bsn        | 123456789                            |
      | anpId      | null                                 |
      | lastName   | Johnson                              |

  Scenario: Retrieve a person by ANP_ID as an admin user
    Given the user is authenticated with normal access rights
    When the user reads a person with ANP_ID "ANP123456"
    Then the user receives status code 200
    And the user receives a person with the following details:
      | id         | 223e4567-e89b-12d3-a456-426614174001 |
      | source     | ANP                                  |
      | bsn        | null                                 |
      | anpId      | ANP123456                            |
      | lastName   | Smith                                |

  # Negative test scenarios
  Scenario: Attempt to retrieve person with non-existent ID
    Given the user is authenticated with normal access rights
    When the user reads a person with ID "999e4567-e89b-12d3-a456-426614174999"
    Then the user receives status code 404
    And the user receives an error message "Person not found"

  Scenario: Attempt to retrieve person with non-existent BSN
    Given the user is authenticated with normal access rights
    When the user reads a person with BSN "999888777"
    Then the user receives status code 404
    And the user receives an error message "Person not found"

  Scenario: Attempt to retrieve person with non-existent ANP_ID
    Given the user is authenticated with normal access rights
    When the user reads a person with ANP_ID "ANP999888"
    Then the user receives status code 404
    And the user receives an error message "Person not found"

  Scenario: Attempt to retrieve person without providing any identifier
    Given the user is authenticated with normal access rights
    When the user reads a person without providing BSN or ANP_ID
    Then the user receives status code 400
    And the user receives an error message "At least one search parameter is required"

  Scenario: Attempt to access person with insufficient permissions
    Given the user is authenticated with normal access rights
    When the user reads a person with ID "323e4567-e89b-12d3-a456-426614174002"
    Then the user receives status code 403
    And the user receives an error message "Insufficient permissions to access this person's details"

  # BSN validation scenarios
  Scenario: Attempt to retrieve person with BSN that fails 11-proef validation
    Given the user is authenticated with normal access rights
    When the user reads a person with BSN "123456788"
    Then the user receives status code 400
    And the user receives an error message "Invalid BSN: does not comply with the 11-test"

  Scenario: Attempt to retrieve person with BSN less than minimum value
    Given the user is authenticated with normal access rights
    When the user reads a person with BSN "099999999"
    Then the user receives status code 400
    And the user receives an error message "BSN must be greater than 100000000"

  Scenario: Successfully retrieve person with valid BSN that passes 11-proef
    Given the user is authenticated with normal access rights
    When the user reads a person with BSN "619631941"
    Then the user receives status code 200
    And the user receives a person with the following details:
      | id         | 823e4567-e89b-12d3-a456-426614174007 |
      | bsn        | 619631941                            |
      | lastName   | Williams                             |

  # Corner cases - BSN/ANP_ID exclusivity
  Scenario: Attempt to read a person with both BSN and ANP_ID
    Given the user is authenticated with normal access rights
    When the user reads a person with BSN "123456789" and ANP_ID "ANP123456"
    Then the user receives status code 400
    And the user receives an error message "Both BSN and ANP_ID provided; only one should be provided"

  Scenario: Retrieve person with missing BSN but valid ANP_ID
    Given the user is authenticated with normal access rights
    When the user reads a person with ANP_ID "ANP111222"
    Then the user receives status code 200
    And the user receives a person with the following details:
      | id         | 623e4567-e89b-12d3-a456-426614174005 |
      | source     | ANP                                  |
      | anpId      | ANP111222                            |
      | lastName   | Wilson                               |
      | dateOfBirth| null                                 |

  Scenario: Retrieve person with missing ANP_ID but valid BSN
    Given the user is authenticated with normal access rights
    When the user reads a person with BSN "123456789"
    Then the user receives status code 200
    And the user receives a person with the following details:
      | id         | 123e4567-e89b-12d3-a456-426614174000 |
      | source     | BRP                                  |
      | bsn        | 123456789                            |
      | anpId      | null                                 |
      | lastName   | Johnson                              |

  # Email validation scenarios
  Scenario: Attempt to retrieve a person with invalid email format
    Given the user is authenticated with normal access rights
    When the user creates a person with email "invalid-email@"
    Then the user receives status code 400
    And the user receives an error message "Invalid email format"

  Scenario: Attempt to retrieve a person with email containing illegal characters
    Given the user is authenticated with normal access rights
    When the user creates a person with email "test#$%^@example.com"
    Then the user receives status code 400
    And the user receives an error message "Email contains illegal characters"

  # Additional corner cases
  Scenario: Retrieve person with invalid format BSN
    Given the user is authenticated with normal access rights
    When the user reads a person with BSN "12345ABCD"
    Then the user receives status code 400
    And the user receives an error message "Invalid BSN format"

  Scenario: Retrieve person with invalid format ANP_ID
    Given the user is authenticated with normal access rights
    When the user reads a person with ANP_ID "123456"
    Then the user receives status code 400
    And the user receives an error message "Invalid ANP_ID format"

  # Authentication scenarios
  Scenario: Attempt to retrieve person without authentication
    When the user reads a person with ID "123e4567-e89b-12d3-a456-426614174000" without authentication
    Then the user receives status code 401
    And the user receives an error message "Authentication required"

  Scenario: Attempt to retrieve person with expired authentication token
    Given the user has an expired authentication token
    When the user reads a person with ID "123e4567-e89b-12d3-a456-426614174000"
    Then the user receives status code 401
    And the user receives an error message "Authentication token expired"

  Scenario: Attempt to retrieve person with invalid authentication token
    Given the user has an invalid authentication token
    When the user reads a person with ID "123e4567-e89b-12d3-a456-426614174000"
    Then the user receives status code 401
    And the user receives an error message "Invalid authentication token"

  # Invalid UUID format scenarios  
  Scenario: Attempt to retrieve person with malformed UUID
    Given the user is authenticated with normal access rights
    When the user reads a person with ID "not-a-valid-uuid"
    Then the user receives status code 400
    And the user receives an error message "Invalid UUID format"

  # Performance scenarios
  Scenario: Verify response time for person retrieval
    Given the user is authenticated with normal access rights
    When the user reads a person with ID "123e4567-e89b-12d3-a456-426614174000"
    Then the user receives status code 200
    And the response time should be less than 500 milliseconds

  # Security scenarios
  Scenario: Attempt SQL injection via ID parameter
    Given the user is authenticated with normal access rights
    When the user reads a person with ID "' OR 1=1; --"
    Then the user receives status code 400
    And the user receives an error message "Invalid UUID format"
    And the system should log the attempted injection

  # API behavior scenarios
  Scenario: Verify API returns content-type application/json
    Given the user is authenticated with normal access rights
    When the user reads a person with ID "123e4567-e89b-12d3-a456-426614174000"
    Then the user receives status code 200
    And the response content type should be "application/json"

  Scenario: API remains responsive under load
    Given the user is authenticated with normal access rights
    When the user sends 50 concurrent requests to read different persons
    Then all responses should be received within 2 seconds
    And at least 95% of responses should have status code 200