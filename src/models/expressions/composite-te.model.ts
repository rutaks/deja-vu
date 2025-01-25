import { TemporalExpression } from "../../interfaces";

export abstract class CompositeTE implements TemporalExpression {
  protected constructor(protected readonly expressions: TemporalExpression[]) {
    if (!expressions?.length) {
      throw new Error(
        "CompositeTemporalExpression requires at least one sub-expression."
      );
    }
  }

  abstract includes(date: Date): boolean;

  protected static checkExpressionsForNull(
    expressions: TemporalExpression[]
  ): void {
    if (expressions.some((e) => e === null)) {
      throw new Error("Null expression found in composite");
    }
  }
}
