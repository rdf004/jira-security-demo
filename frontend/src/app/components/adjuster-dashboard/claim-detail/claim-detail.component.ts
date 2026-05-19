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
  selector: 'app-claim-detail',
  templateUrl:
    './claim-detail.component.html',
  styleUrls:
    ['./claim-detail.component.css']
})
export class ClaimDetailComponent
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
        return 'badge-success';
      case 'ADJUSTER_REVIEW':
        return 'badge-warning';
      case 'FRAUD_MANUAL_INVESTIGATION':
        return 'badge-danger';
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
