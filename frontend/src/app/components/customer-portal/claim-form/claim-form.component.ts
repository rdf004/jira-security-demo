import {
  Component,
  OnInit
} from '@angular/core';
import {
  ActivatedRoute,
  Router
} from '@angular/router';
import {
  ApiService
} from '../../../services/api.service';
import {
  Policy,
  ClaimSubmission,
  CLAIM_TYPES
} from '../../../models/models';

@Component({
  selector: 'app-claim-form',
  templateUrl:
    './claim-form.component.html',
  styleUrls:
    ['./claim-form.component.css']
})
export class ClaimFormComponent
  implements OnInit {

  policy: Policy;
  claimTypes = CLAIM_TYPES;
  availableTypes: any[] = [];

  claim: ClaimSubmission = {
    policyId: 0,
    claimType: '',
    incidentDate: '',
    incidentLocation: '',
    description: '',
    estimatedLoss: 0,
    documents: []
  };

  documentInput = '';
  errors: string[] = [];
  submitting = false;
  loading = true;

  claimTypeQuestions: {
    [key: string]: string[]
  } = {
    'AUTO_COLLISION': [
      'Were there other vehicles involved?',
      'Was a police report filed?',
      'Was anyone injured?'
    ],
    'HOME_WATER_DAMAGE': [
      'Where did the water damage occur?',
      'Is the damage ongoing or contained?',
      'Have you contacted a plumber?'
    ],
    'TRAVEL_DELAYED_BAGGAGE': [
      'Which airline and flight number?',
      'How long was the delay?',
      'Did you file a report at the airport?'
    ],
    'HEALTH_REIMBURSEMENT': [
      'What type of medical service?',
      'Was this an emergency visit?',
      'Do you have a doctor referral?'
    ]
  };

  dynamicAnswers: string[] = [];

  constructor(
    private api: ApiService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    const id = +this.route.snapshot
      .paramMap.get('policyId');
    this.claim.policyId = id;
    this.api.getPolicy(id)
      .subscribe(
        policy => {
          this.policy = policy;
          this.availableTypes =
            this.claimTypes.filter(
              t => policy.coveredClaimTypes
                .indexOf(t.value) !== -1
            );
          this.loading = false;
        },
        () => {
          this.errors = [
            'Failed to load policy'
          ];
          this.loading = false;
        }
      );
  }

  getQuestions(): string[] {
    if (!this.claim.claimType) {
      return [];
    }
    return this.claimTypeQuestions[
      this.claim.claimType
    ] || [];
  }

  onClaimTypeChange() {
    const qs = this.getQuestions();
    this.dynamicAnswers =
      new Array(qs.length).fill('');
  }

  addDocument() {
    const doc = this.documentInput.trim();
    if (doc) {
      this.claim.documents.push(doc);
      this.documentInput = '';
    }
  }

  removeDocument(index: number) {
    this.claim.documents.splice(index, 1);
  }

  submit() {
    this.errors = [];
    if (!this.claim.claimType) {
      this.errors.push(
        'Please select a claim type'
      );
    }
    if (!this.claim.incidentDate) {
      this.errors.push(
        'Please enter the incident date'
      );
    }
    if (!this.claim.description) {
      this.errors.push(
        'Please enter a description'
      );
    }
    if (!this.claim.estimatedLoss
        || this.claim.estimatedLoss <= 0) {
      this.errors.push(
        'Please enter estimated loss amount'
      );
    }
    if (this.errors.length > 0) {
      return;
    }

    let desc = this.claim.description;
    const qs = this.getQuestions();
    if (qs.length > 0) {
      desc += '\n\nAdditional Details:\n';
      for (let i = 0; i < qs.length; i++) {
        if (this.dynamicAnswers[i]) {
          desc += qs[i] + ' '
            + this.dynamicAnswers[i] + '\n';
        }
      }
    }

    const submission: ClaimSubmission = {
      ...this.claim,
      description: desc
    };

    this.submitting = true;
    this.api.submitClaim(submission)
      .subscribe(
        (result: any) => {
          this.submitting = false;
          if (result && result.id) {
            this.router.navigate(
              ['/claim/confirmation',
               result.id]
            );
          }
        },
        (err) => {
          this.submitting = false;
          if (err.error && err.error.errors) {
            this.errors = err.error.errors;
          } else {
            this.errors = [
              'Failed to submit claim'
            ];
          }
        }
      );
  }
}
