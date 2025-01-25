import { TemporalExpression } from "../../interfaces";
import { CompositeTE } from "./composite-te.model";

export class IntersectionTE extends CompositeTE {
  private constructor(expressions: TemporalExpression[]) {
    super(expressions);
  }

  static of(expressions: TemporalExpression[]): IntersectionTE {
    CompositeTE.checkExpressionsForNull(expressions);
    return new IntersectionTE(expressions);
  }

  includes(date: Date): boolean {
    return this.expressions.every((e) => e.includes(date));
  }
}
