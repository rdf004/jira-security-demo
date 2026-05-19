import {
  Component,
  OnInit
} from '@angular/core';
import { Router } from '@angular/router';
import {
  ApiService
} from '../../../services/api.service';
import {
  Claim
} from '../../../models/models';

@Component({
  selector: 'app-claims-table',
  templateUrl:
    './claims-table.component.html',
  styleUrls:
    ['./claims-table.component.css']
})
export class ClaimsTableComponent
  implements OnInit {

  claims: Claim[] = [];
  filteredClaims: Claim[] = [];
  loading = true;
  filterStatus = '';

  constructor(
    private api: ApiService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadClaims();
  }

  loadClaims() {
    this.api.getClaims()
      .subscribe(data => {
        this.claims = data;
        this.applyFilter();
        this.loading = false;
      });
  }

  applyFilter() {
    if (!this.filterStatus) {
      this.filteredClaims = this.claims;
    } else {
      this.filteredClaims =
        this.claims.filter(
          c => c.triageResult ===
            this.filterStatus
        );
    }
  }

  viewDetail(id: number) {
    this.router.navigate(
      ['/dashboard/claim', id]
    );
  }

  getStatusClass(
    triageResult: string
  ): string {
    switch (triageResult) {
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

  getStatusLabel(
    triageResult: string
  ): string {
    switch (triageResult) {
      case 'STRAIGHT_THROUGH_PROCESSING':
        return 'Straight-Through';
      case 'ADJUSTER_REVIEW':
        return 'Adjuster Review';
      case 'FRAUD_MANUAL_INVESTIGATION':
        return 'Fraud/Investigation';
      default:
        return triageResult;
    }
  }
}
