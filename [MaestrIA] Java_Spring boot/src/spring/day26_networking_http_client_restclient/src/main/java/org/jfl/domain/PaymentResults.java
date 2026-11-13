package org.jfl.domain;

sealed public interface PaymentResults permits Approved, Declined{
}
