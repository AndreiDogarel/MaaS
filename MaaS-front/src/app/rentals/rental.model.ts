export interface Rental {
  id: number;
  vehicleId: number;
  userId: number;
  startDate: string;
  endDate?: string | null;
  status: string;
  odometerStart?: number | null;
  odometerEnd?: number | null;
  totalPrice?: number | null;
}
