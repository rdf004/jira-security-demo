# Smart Claims Intake & Triage Portal

A lightweight, realistic P&C insurance demo application
built for **Generali**. Customers submit claims, the
system validates policy coverage, and adjusters receive
structured claim summaries with routing recommendations.

## Tech Stack

| Layer    | Technology                    |
|----------|-------------------------------|
| Backend  | Java 8, Spring Boot 2.7, JPA |
| Frontend | Angular 7, TypeScript         |
| Database | H2 (in-memory)               |
| Build    | Maven, npm / Angular CLI      |

## How to Run

### Prerequisites

- **Java 8** (JDK 1.8)
- **Node.js 10.x** (for Angular 7)
- **Maven 3.6+**

### Backend

```bash
cd backend
export JAVA_HOME=/path/to/java8
mvn clean spring-boot:run
```

Backend starts on `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
npx ng serve
```

Frontend starts on `http://localhost:4200`.

### Run Tests

```bash
cd backend
mvn test
```

14 unit tests cover policy validation, coverage
date checks, claim type coverage, missing document
validation, and triage/routing classification.

## Architecture

```
jira-security-demo/
├── backend/
│   └── src/main/java/com/generali/claims/
│       ├── config/         # CORS, data seeder
│       ├── controller/     # REST endpoints
│       ├── model/          # JPA entities, DTOs
│       ├── repository/     # Spring Data repos
│       ├── rules/          # Triage rule engine
│       └── service/        # Business logic
├── frontend/
│   └── src/app/
│       ├── components/
│       │   ├── header/
│       │   ├── customer-portal/
│       │   │   ├── policy-list/
│       │   │   ├── claim-form/
│       │   │   └── claim-confirmation/
│       │   └── adjuster-dashboard/
│       │       ├── claims-table/
│       │       └── claim-detail/
│       ├── models/
│       └── services/
└── README.md
```

## Key Files

| File | Purpose |
|------|---------|
| `backend/.../rules/TriageRuleEngine.java` | Core routing logic |
| `backend/.../rules/TriageRuleConfig.java` | Rule thresholds |
| `backend/src/main/resources/rules/triage-rules.json` | Configurable rule values |
| `backend/.../service/PolicyValidationService.java` | Coverage validation |
| `backend/.../service/TriageService.java` | Triage orchestration |
| `backend/.../config/DataSeeder.java` | Mock data (4 customers, 7 policies) |
| `frontend/src/app/models/models.ts` | TypeScript interfaces |
| `frontend/src/app/services/api.service.ts` | HTTP client |

## Core Flows

### 1. Customer Claim Submission

- Browse active policies at `/policies`
- Select a policy and click "File New Claim"
- Choose claim type (form shows dynamic questions)
- Enter incident details and attach documents
- Submit the claim

### 2. Policy & Coverage Validation

The backend validates:
- Policy exists and is active
- Incident date within coverage period
- Claim type is covered by the policy
- Required documents are present

### 3. Triage & Routing

Claims are classified using configurable rules:

| Route | Condition |
|-------|-----------|
| Straight-through | Low value (<=\$2K) + complete docs |
| Adjuster review | Medium/high value or missing docs |
| Fraud/investigation | Expired policy, duplicate claim, or amount >\$25K |

Rules are in `triage-rules.json` and can be changed
without modifying code.

### 4. Adjuster Dashboard

- View all claims at `/dashboard`
- Filter by routing result
- Click any claim for full detail view
- Includes adjuster summary in plain English

## Demo Script

1. Open `http://localhost:4200`
2. See Maria Rossi's **Home** policy (POL-HOME-001)
3. Click "File New Claim" on the Home policy
4. Select "Home Water Damage", enter:
   - Date: 2025-06-15
   - Location: Milan, Via Roma 42
   - Description: "Burst pipe in kitchen caused
     water damage to floor and cabinets"
   - Estimated Loss: $800
   - Add documents: "Photos of damage",
     "Repair estimate"
5. Submit — routed to **Straight-Through Processing**
6. Go back, select Maria's **Auto** policy
7. File an Auto Collision claim:
   - Date: 2025-07-01
   - Estimated Loss: $12,000
   - Add only "Photos of damage" (skip police report)
8. Submit — routed to **Adjuster Review** with
   missing document warning
9. Click "Adjuster Dashboard" in the nav
10. See both claims with routing badges
11. Click a claim to see the full detail and
    adjuster summary

## Business Rule Extensibility

- **Add a new claim type**: Add to `ClaimType.java`
  enum, update seed data in `DataSeeder.java`,
  and add questions in `claim-form.component.ts`.
- **Modify routing rules**: Edit
  `triage-rules.json` to change dollar thresholds
  or the duplicate-claim window.
- **Add a new rule**: Extend
  `TriageRuleEngine.evaluate()` with new conditions.

## Example Future Devin Tasks

- "Add a Pet Insurance claim type with vet receipt
  as a required document"
- "Lower the straight-through threshold to $1,500"
- "Add email notification when a claim is flagged
  for fraud investigation"
- "Add pagination to the adjuster dashboard"
- "Create a REST endpoint to update claim status"
- "Add date range filtering to the dashboard"

## Design

UI follows the **Generali brand style guide**:
- Primary Red: `#C21B17`
- Font: Roboto (300/400/500/700)
- 5px border-radius buttons, uppercase labels
- Diagonal hero motif on the policy list page
- Clean institutional aesthetic with alternating
  white/gray sections
