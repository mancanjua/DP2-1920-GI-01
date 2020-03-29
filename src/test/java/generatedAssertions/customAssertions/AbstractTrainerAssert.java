package generatedAssertions.customAssertions;

import org.assertj.core.api.AbstractObjectAssert; 
import org.assertj.core.util.Objects;
import org.springframework.samples.petclinic.model.Trainer;

/**
 * Abstract base class for {@link Trainer} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractTrainerAssert<S extends AbstractTrainerAssert<S, A>, A extends Trainer> extends AbstractObjectAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractTrainerAssert}</code> to make assertions on actual Trainer.
   * @param actual the Trainer we want to make assertions on.
   */
  protected AbstractTrainerAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual Trainer's email is equal to the given one.
   * @param email the given email to compare the actual Trainer's email to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Trainer's email is not equal to the given one.
   */
  public S hasEmail(String email) {
    // check that actual Trainer we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting email of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualEmail = actual.getEmail();
    if (!Objects.areEqual(actualEmail, email)) {
      failWithMessage(assertjErrorMessage, actual, email, actualEmail);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Trainer's phone is equal to the given one.
   * @param phone the given phone to compare the actual Trainer's phone to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Trainer's phone is not equal to the given one.
   */
  public S hasPhone(String phone) {
    // check that actual Trainer we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting phone of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualPhone = actual.getPhone();
    if (!Objects.areEqual(actualPhone, phone)) {
      failWithMessage(assertjErrorMessage, actual, phone, actualPhone);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Trainer's user is equal to the given one.
   * @param user the given user to compare the actual Trainer's user to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Trainer's user is not equal to the given one.
   */
  public S hasUser(org.springframework.samples.petclinic.model.User user) {
    // check that actual Trainer we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting user of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    org.springframework.samples.petclinic.model.User actualUser = actual.getUser();
    if (!Objects.areEqual(actualUser, user)) {
      failWithMessage(assertjErrorMessage, actual, user, actualUser);
    }

    // return the current assertion for method chaining
    return myself;
  }

}
