import { Rental } from "../rentals/rental.model";

export interface Vehicle {
  id?: number;
  registrationNumber: string;
  brand: string;
  model: string;
  year: number;
  mileage: number;
  licenseCategory: string;
  status: string;
  pricePerDay: number;
  rentals?: Rental[];
}
