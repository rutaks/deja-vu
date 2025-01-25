import { TemporalExpression } from "../../interfaces";

export class DifferenceTE implements TemporalExpression {
  private constructor(
    private readonly included: TemporalExpression,
    private readonly excluded: TemporalExpression
  ) {}

  static of(
    included: TemporalExpression,
    excluded: TemporalExpression
  ): DifferenceTE {
    return new DifferenceTE(included, excluded);
  }

  includes(date: Date): boolean {
    return this.included.includes(date) && !this.excluded.includes(date);
  }
}
