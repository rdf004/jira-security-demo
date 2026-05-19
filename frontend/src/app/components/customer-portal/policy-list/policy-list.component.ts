import {
  Component,
  OnInit
} from '@angular/core';
import { Router } from '@angular/router';
import {
  ApiService
} from '../../../services/api.service';
import {
  Policy
} from '../../../models/models';

@Component({
  selector: 'app-policy-list',
  templateUrl:
    './policy-list.component.html',
  styleUrls:
    ['./policy-list.component.css']
})
export class PolicyListComponent
  implements OnInit {

  policies: Policy[] = [];
  loading = true;

  constructor(
    private api: ApiService,
    private router: Router
  ) {}

  ngOnInit() {
    this.api.getActivePolicies()
      .subscribe(data => {
        this.policies = data;
        this.loading = false;
      });
  }

  startClaim(policyId: number) {
    this.router.navigate(
      ['/claim/new', policyId]
    );
  }
}
