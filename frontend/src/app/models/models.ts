export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  fullName: string;
}

export interface Policy {
  id: number;
  policyNumber: string;
  policyType: string;
  customer: Customer;
  active: boolean;
  startDate: string;
  endDate: string;
  coveredClaimTypes: string[];
  requiredDocuments: string[];
}

export interface Claim {
  id: number;
  claimNumber: string;
  policy: Policy;
  claimType: string;
  incidentDate: string;
  incidentLocation: string;
  description: string;
  estimatedLoss: number;
  documents: string[];
  status: string;
  triageResult: string;
  triageReasons: string[];
  missingDocuments: string[];
  adjusterSummary: string;
  submittedAt: string;
}

export interface ClaimSubmission {
  policyId: number;
  claimType: string;
  incidentDate: string;
  incidentLocation: string;
  description: string;
  estimatedLoss: number;
  documents: string[];
}

export interface ValidationResult {
  valid: boolean;
  errors: string[];
  missingDocuments: string[];
}

export const CLAIM_TYPES = [
  {
    value: 'AUTO_COLLISION',
    label: 'Auto Collision'
  },
  {
    value: 'HOME_WATER_DAMAGE',
    label: 'Home Water Damage'
  },
  {
    value: 'TRAVEL_DELAYED_BAGGAGE',
    label: 'Travel Delayed Baggage'
  },
  {
    value: 'HEALTH_REIMBURSEMENT',
    label: 'Health Reimbursement'
  }
];
