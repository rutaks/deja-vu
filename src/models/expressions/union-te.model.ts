import { TemporalExpression } from "../../interfaces";
import { CompositeTE } from "./composite-te.model";

export class UnionTE extends CompositeTE {
  private constructor(expressions: TemporalExpression[]) {
    super(expressions);
  }

  static of(expressions: TemporalExpression[]): UnionTE {
    CompositeTE.checkExpressionsForNull(expressions);
    return new UnionTE(expressions);
  }

  includes(date: Date): boolean {
    return this.expressions.some((e) => e.includes(date));
  }
}
