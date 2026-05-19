import { NgModule } from '@angular/core';
import {
  Routes,
  RouterModule
} from '@angular/router';
import {
  PolicyListComponent
} from
  './components/customer-portal/policy-list/policy-list.component';
import {
  ClaimFormComponent
} from
  './components/customer-portal/claim-form/claim-form.component';
import {
  ClaimConfirmationComponent
} from
  './components/customer-portal/claim-confirmation/claim-confirmation.component';
import {
  ClaimsTableComponent
} from
  './components/adjuster-dashboard/claims-table/claims-table.component';
import {
  ClaimDetailComponent
} from
  './components/adjuster-dashboard/claim-detail/claim-detail.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/policies',
    pathMatch: 'full'
  },
  {
    path: 'policies',
    component: PolicyListComponent
  },
  {
    path: 'claim/new/:policyId',
    component: ClaimFormComponent
  },
  {
    path: 'claim/confirmation/:id',
    component: ClaimConfirmationComponent
  },
  {
    path: 'dashboard',
    component: ClaimsTableComponent
  },
  {
    path: 'dashboard/claim/:id',
    component: ClaimDetailComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
