/**
 * @return {String}
 */
function sayHello() {
  /*
  this line is fine, but the following is not
  if (something) {

  no issue on the following line, because there is at most one violation per comment
  if (something) {
  */

  // the following line is not ok
  // if (something) {

  // good

  // ====
  // ----
  // ++++

  // good
  // http://www.example.com/ = http://www.example.com/
}
