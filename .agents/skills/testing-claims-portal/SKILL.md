---
name: testing-claims-portal
description: Test the Smart Claims Portal end-to-end. Use when verifying claim submission, triage routing, or adjuster dashboard changes.
---

# Testing the Smart Claims Portal

## Prerequisites

- Java 8+ and Maven installed
- Node.js 10+ and npm installed
- Ports 8080 (backend) and 4200 (frontend) available

## Starting the Application

### Backend
```bash
cd backend && mvn spring-boot:run
```
Wait for "Started ClaimsApplication" in the logs.
Verify: `curl http://localhost:8080/api/policies/active`
should return JSON array of active policies.

### Frontend
```bash
cd frontend && npm install && npx ng serve
```
Wait for "Compiled successfully".
Verify: `curl -s http://localhost:4200` should return HTML.

## Key URLs

| Page | URL |
|------|-----|
| Policy List | http://localhost:4200/policies |
| Claim Form | http://localhost:4200/claim/new/{policyId} |
| Confirmation | http://localhost:4200/claim/confirmation/{id} |
| Dashboard | http://localhost:4200/dashboard |
| Claim Detail | http://localhost:4200/dashboard/claim/{id} |

## Seed Data

The H2 in-memory DB auto-populates on startup with:
- 4 customers, 7 policies (6 active, 1 inactive)
- Active policy IDs: 1 (Home), 2 (Auto), 3 (Travel),
  4 (Health), 5 (Home), 7 (Health)
- Data resets on backend restart

## Triage Routing Thresholds

Defined in `backend/src/main/resources/rules/triage-rules.json`:
- Amount <= $2,000 with complete docs: Straight-Through Processing
- Amount $2,000-$25,000: Adjuster Review
- Amount > $25,000: Fraud/Manual Investigation
- Missing required documents: escalates to Adjuster Review
- Inactive policy or duplicate claim within 30 days:
  Fraud/Manual Investigation

## Primary Test Flow

### Test 1: Low-Value Claim → Straight-Through
1. Go to /policies, click FILE NEW CLAIM on POL-HOME-001
2. Select "Home Water Damage"
3. Fill: date within coverage, loss=$500, add both required
   docs ("Photos of damage", "Repair estimate")
4. Submit → confirmation should show
   "Straight-Through Processing"

### Test 2: High-Value Claim → Adjuster Review
1. Go to /policies, click FILE NEW CLAIM on POL-AUTO-002
2. Select "Auto Collision"
3. Fill: date within coverage, loss=$12000,
   add only 1 of 3 required docs
4. Submit → confirmation should show "Adjuster Review"
   with missing documents listed

### Test 3: Dashboard Filtering
1. Go to /dashboard
2. Filter by each routing type and verify correct counts
3. Click a claim row to view detail page with
   adjuster summary

## Required Documents by Policy Type

- Home: "Photos of damage", "Repair estimate"
- Auto: "Police report", "Photos of damage",
  "Repair estimate"
- Travel: "Boarding pass", "Baggage claim receipt"
- Health: "Medical receipt", "Doctor referral"

## Known Issues

- Hibernate MultipleBagFetchException might occur if
  entity collections are changed from Set back to List.
  All @ElementCollection fields on Policy and Claim
  entities must use Set, not List.
- The H2 database resets on restart, so previously
  submitted claims will not persist.

## Unit Tests

```bash
cd backend && mvn clean test
```
14 tests: 8 PolicyValidationServiceTest +
6 TriageServiceTest. All should pass.

## Devin Secrets Needed

None. The app uses an embedded H2 database and
requires no external credentials.
