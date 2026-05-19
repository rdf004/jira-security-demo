import {
  BrowserModule
} from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {
  HttpClientModule
} from '@angular/common/http';
import {
  FormsModule,
  ReactiveFormsModule
} from '@angular/forms';
import {
  AppRoutingModule
} from './app-routing.module';
import {
  AppComponent
} from './app.component';
import {
  HeaderComponent
} from
  './components/header/header.component';
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

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    PolicyListComponent,
    ClaimFormComponent,
    ClaimConfirmationComponent,
    ClaimsTableComponent,
    ClaimDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
