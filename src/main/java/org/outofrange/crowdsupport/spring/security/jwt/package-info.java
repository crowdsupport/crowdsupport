/**
 * This package contains all elements necessary to perform authentication based on JSON Web Tokens.
 * <p/>
 * The examples are from
 * <a href="http://blog.jdriven.com/2014/10/stateless-spring-security-part-2-stateless-authentication/">jdriven</a>,
 * but they had to be heavily adapted to work correctly and conform to the JWT standard.
 * <p/>
 * Some differences to the blogged implementation are:
 * <ul>
 *     <li>
 *         The main advantage of JWT is that it's not necessary to query the database for authentication.
 *         jdriven's implementation was doing exactly that.
 *     </li>
 *     <li>
 *         JWTs, as defined by the standard, consist of three parts: a header, a payload and the signature.
 *         jdriven's implementation was missing the header, making it impossible to parse tokens by other libraries
 *         conforming to the standard.
 *     </li>
 * </ul>
 */
package org.outofrange.crowdsupport.spring.security.jwt;