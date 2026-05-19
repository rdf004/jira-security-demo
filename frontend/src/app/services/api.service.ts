import { Injectable } from '@angular/core';
import {
  HttpClient
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Customer,
  Policy,
  Claim,
  ClaimSubmission
} from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(
      `${this.baseUrl}/customers`
    );
  }

  getPolicies(): Observable<Policy[]> {
    return this.http.get<Policy[]>(
      `${this.baseUrl}/policies`
    );
  }

  getActivePolicies(): Observable<Policy[]> {
    return this.http.get<Policy[]>(
      `${this.baseUrl}/policies/active`
    );
  }

  getPoliciesByCustomer(
    customerId: number
  ): Observable<Policy[]> {
    const url =
      `${this.baseUrl}/policies`
      + `/customer/${customerId}`;
    return this.http.get<Policy[]>(url);
  }

  getPolicy(
    id: number
  ): Observable<Policy> {
    return this.http.get<Policy>(
      `${this.baseUrl}/policies/${id}`
    );
  }

  getClaims(): Observable<Claim[]> {
    return this.http.get<Claim[]>(
      `${this.baseUrl}/claims`
    );
  }

  getClaim(id: number): Observable<Claim> {
    return this.http.get<Claim>(
      `${this.baseUrl}/claims/${id}`
    );
  }

  submitClaim(
    claim: ClaimSubmission
  ): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/claims`, claim
    );
  }
}
