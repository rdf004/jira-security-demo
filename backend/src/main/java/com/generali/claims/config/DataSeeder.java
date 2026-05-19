package com.generali.claims.config;

import com.generali.claims.model.ClaimType;
import com.generali.claims.model.Customer;
import com.generali.claims.model.Policy;
import com.generali.claims.repository
    .CustomerRepository;
import com.generali.claims.repository
    .PolicyRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder {

    private final CustomerRepository custRepo;
    private final PolicyRepository policyRepo;

    public DataSeeder(
        CustomerRepository custRepo,
        PolicyRepository policyRepo
    ) {
        this.custRepo = custRepo;
        this.policyRepo = policyRepo;
    }

    @PostConstruct
    public void seed() throws ParseException {
        if (custRepo.count() > 0) {
            return;
        }
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd");

        Customer c1 = custRepo.save(
            new Customer(
                "Maria", "Rossi",
                "maria.rossi@email.com",
                "+39 02 1234567"
            )
        );
        Customer c2 = custRepo.save(
            new Customer(
                "Luca", "Bianchi",
                "luca.bianchi@email.com",
                "+39 06 7654321"
            )
        );
        Customer c3 = custRepo.save(
            new Customer(
                "Sofia", "Ferrari",
                "sofia.ferrari@email.com",
                "+39 055 9876543"
            )
        );
        Customer c4 = custRepo.save(
            new Customer(
                "Marco", "Conti",
                "marco.conti@email.com",
                "+39 011 5551234"
            )
        );

        createPolicy(
            "POL-HOME-001", "Home",
            c1, true,
            sdf.parse("2025-01-01"),
            sdf.parse("2026-01-01"),
            new ClaimType[]{
                ClaimType.HOME_WATER_DAMAGE
            },
            new String[]{
                "Photos of damage",
                "Repair estimate"
            }
        );
        createPolicy(
            "POL-AUTO-002", "Auto",
            c1, true,
            sdf.parse("2025-01-01"),
            sdf.parse("2026-01-01"),
            new ClaimType[]{
                ClaimType.AUTO_COLLISION
            },
            new String[]{
                "Police report",
                "Photos of damage",
                "Repair estimate"
            }
        );
        createPolicy(
            "POL-TRVL-003", "Travel",
            c2, true,
            sdf.parse("2025-03-01"),
            sdf.parse("2025-09-01"),
            new ClaimType[]{
                ClaimType
                    .TRAVEL_DELAYED_BAGGAGE
            },
            new String[]{
                "Airline delay confirmation",
                "Baggage claim receipt"
            }
        );
        createPolicy(
            "POL-HLTH-004", "Health",
            c2, true,
            sdf.parse("2025-01-01"),
            sdf.parse("2026-01-01"),
            new ClaimType[]{
                ClaimType
                    .HEALTH_REIMBURSEMENT
            },
            new String[]{
                "Medical receipt",
                "Doctor referral"
            }
        );
        createPolicy(
            "POL-HOME-005", "Home",
            c3, true,
            sdf.parse("2025-02-01"),
            sdf.parse("2026-02-01"),
            new ClaimType[]{
                ClaimType.HOME_WATER_DAMAGE
            },
            new String[]{
                "Photos of damage",
                "Repair estimate"
            }
        );
        createPolicy(
            "POL-AUTO-006", "Auto",
            c3, false,
            sdf.parse("2023-01-01"),
            sdf.parse("2024-01-01"),
            new ClaimType[]{
                ClaimType.AUTO_COLLISION
            },
            new String[]{
                "Police report",
                "Photos of damage"
            }
        );
        createPolicy(
            "POL-HLTH-007", "Health",
            c4, true,
            sdf.parse("2025-01-01"),
            sdf.parse("2026-01-01"),
            new ClaimType[]{
                ClaimType
                    .HEALTH_REIMBURSEMENT
            },
            new String[]{
                "Medical receipt",
                "Doctor referral"
            }
        );
    }

    private Policy createPolicy(
        String number,
        String type,
        Customer customer,
        boolean active,
        Date start,
        Date end,
        ClaimType[] coveredTypes,
        String[] requiredDocs
    ) {
        Policy p = new Policy();
        p.setPolicyNumber(number);
        p.setPolicyType(type);
        p.setCustomer(customer);
        p.setActive(active);
        p.setStartDate(start);
        p.setEndDate(end);
        p.setCoveredClaimTypes(
            new HashSet<>(
                Arrays.asList(coveredTypes)
            )
        );
        p.setRequiredDocuments(
            new HashSet<>(
                Arrays.asList(requiredDocs)
            )
        );
        return policyRepo.save(p);
    }
}
