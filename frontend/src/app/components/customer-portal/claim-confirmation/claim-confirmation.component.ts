import {
  Component,
  OnInit
} from '@angular/core';
import {
  ActivatedRoute
} from '@angular/router';
import {
  ApiService
} from '../../../services/api.service';
import {
  Claim
} from '../../../models/models';

@Component({
  selector: 'app-claim-confirmation',
  templateUrl:
    './claim-confirmation.component.html',
  styleUrls:
    ['./claim-confirmation.component.css']
})
export class ClaimConfirmationComponent
  implements OnInit {

  claim: Claim;
  loading = true;

  constructor(
    private api: ApiService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const id = +this.route.snapshot
      .paramMap.get('id');
    this.api.getClaim(id)
      .subscribe(data => {
        this.claim = data;
        this.loading = false;
      });
  }

  getStatusClass(): string {
    if (!this.claim) {
      return '';
    }
    switch (this.claim.triageResult) {
      case 'STRAIGHT_THROUGH_PROCESSING':
        return 'status-success';
      case 'ADJUSTER_REVIEW':
        return 'status-warning';
      case 'FRAUD_MANUAL_INVESTIGATION':
        return 'status-danger';
      default:
        return '';
    }
  }

  getStatusLabel(): string {
    if (!this.claim) {
      return '';
    }
    switch (this.claim.triageResult) {
      case 'STRAIGHT_THROUGH_PROCESSING':
        return 'Straight-Through Processing';
      case 'ADJUSTER_REVIEW':
        return 'Adjuster Review';
      case 'FRAUD_MANUAL_INVESTIGATION':
        return 'Fraud/Manual Investigation';
      default:
        return this.claim.triageResult;
    }
  }
}
