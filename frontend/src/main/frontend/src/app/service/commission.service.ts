import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class CommissionService {

  constructor(private http: HttpClient) {
  }

  getCommissionsByListId(listId) {
    return this.http.get("/commissions/" + "?listId=" + listId);
  }

  transferPresidentRole(commissionDto) {
    return this.http.post("/commissions/transferPresidentRole", commissionDto);
  }
}

