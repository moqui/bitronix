/**
 * XAConnectionFactory emulator using Last Resource Commit (LRC) on an underlying non-XA
 * {@code ConnectionFactory}.
 * <p>
 * Note that if you use the classes of this package, you have accepted the <em>heuristic hazard</em>.
 * A crash during commit of a connection returned by this connection factory could lead to an
 * inconsistent global state. This is a limitation of the Last Resource Commit technique, not of BTM.
 * </p>
 */
package bitronix.tm.resource.jms.lrc;
