import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.security.GeneralSecurityException;
import java.util.Base64;


/** A server that keeps up with a public key for every user.  It maintains
    a map, with a location for each user. */
public class Server {
  /** Port number used by the server */
  public static final int PORT_NUMBER = 26188;

  /** Record for an individual user. */
  private static class UserRec {
    // Name of this user.
    String name;

    // Row location of this user.
    int row;

    // Column location of this user.
    int col;

    // Amount of gold this user has.
    int gold;

    // This user's public key.
    PublicKey publicKey;
  }

  /** Record for the map's current state. */
  private static class MapRec {
    // Number of rows in the data
    int rows;
    // Number of columns in the data
    int cols;
    // The 2D character array of data
    char[][] data;
  }

  private static MapRec m = new MapRec();

  /** List of all the user records. */
  private static ArrayList< UserRec > userList = new ArrayList< UserRec >();

  private static void usage() {
    System.err.println( "usage: Server MAP-FILE.TXT" );
    System.exit( 1 );
  }

  /** Read the list of all users and their public keys. */
  private void readUsers() throws Exception {
    Scanner input = new Scanner( new File( "passwd.txt" ) );
    while ( input.hasNext() ) {
      // Create a record for the next user.
      UserRec rec = new UserRec();
      rec.name = input.next();

      // Get the key as a string of hex digits and turn it into a byte array.
      String base64Key = input.nextLine().trim();
      byte[] rawKey = Base64.getDecoder().decode( base64Key );
    
      // Make a key specification based on this key.
      X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec( rawKey );

      // Make an RSA key based on this specification
      KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );
      rec.publicKey = keyFactory.generatePublic( pubKeySpec );

      // Add this user to the list of all users.
      userList.add( rec );
    }
  }
  
  /** Utility function to read a length then a byte array from the
      given stream.  TCP doesn't respect message boundaraies, but this
      is essientially a technique for marking the start and end of
      each message in the byte stream.  As a public, static method,
      this can also be used by the client. */
  public static byte[] getMessage( DataInputStream input ) throws IOException {
    int len = input.readInt();
    byte[] msg = new byte [ len ];
    input.readFully( msg );
    return msg;
  }

  /** Function analogous to the previous one, for sending messages. */
  public static void putMessage( DataOutputStream output, byte[] msg ) throws IOException {
    // Write the length of the given message, followed by its contents.
    output.writeInt( msg.length );
    output.write( msg, 0, msg.length );
    output.flush();
  }

  /** Function to move the player's location up by one row. */
  private static synchronized String north(UserRec playerData) {
    // Check that the location north is not off the edge of the map and doesnt contain a wall
    if(playerData.row > 0 && m.data[playerData.row - 1][playerData.col] != '#') {
      // Check that the location north is not occupied
      for(int i = 0; i < userList.size(); i++) {
        if(userList.get(i).name != playerData.name && userList.get(i).row == playerData.row - 1 && userList.get(i).col == playerData.col  ) {
          // The location north is occupied
          return "Invalid command";
        }
      }

      // The location north is not occupied, execute the move
      playerData.row--;

      return "";
    }
    
    return "Invalid command";
  }

  /** Function to move the player's location left by one column. */
  private static synchronized String west(UserRec playerData) {
    // Check that the location west is not off the edge of the map and doesnt contain a wall
    if(playerData.col > 0 && m.data[playerData.row][playerData.col - 1] != '#') {
      // Check that the location west is not occupied
      for(int i = 0; i < userList.size(); i++) {
        if(userList.get(i).name != playerData.name && userList.get(i).row == playerData.row && userList.get(i).col == playerData.col - 1  ) {
          // The location west is occupied
          return "Invalid command";
        }
      }

      // The location west is not occupied, execute the move
      playerData.col--;

      return "";
    }
    
    return "Invalid command";
  }

  /** Function to move the player's location down by one row. */
  private static synchronized String south(UserRec playerData) {
    // Check that the location south is not off the edge of the map and doesnt contain a wall
    if(playerData.row + 1 < m.rows && m.data[playerData.row + 1][playerData.col] != '#') {
      // Check that the location south is not occupied
      for(int i = 0; i < userList.size(); i++) {
        if(userList.get(i).name != playerData.name && userList.get(i).row == playerData.row + 1 && userList.get(i).col == playerData.col  ) {
          // The location south is occupied
          return "Invalid command";
        }
      }

      // The location south is not occupied, execute the move
      playerData.row++;

      return "";
    }
    
    return "Invalid command";
  }

  /** Function to move the player's location right by one row. */
  private static synchronized String east(UserRec playerData) {
    // Check that the location east is not off the edge of the map and doesnt contain a wall
    if(playerData.col + 1 < m.cols && m.data[playerData.row][playerData.col + 1] != '#') {
      // Check that the location east is not occupied
      for(int i = 0; i < userList.size(); i++) {
        if(userList.get(i).name != playerData.name && userList.get(i).row == playerData.row && userList.get(i).col == playerData.col + 1  ) {
          // The location east is occupied
          return "Invalid command";
        }
      }

      // The location east is not occupied, execute the move
      playerData.col++;

      return "";
    }
    
    return "Invalid command";
  }

  /** Function to return the map's data as a string. */
  private static synchronized String map(UserRec playerData) {
    StringBuilder mapBuilder = new StringBuilder();
    
    // Add all of the map's normal data
    for(int i = 0; i < m.rows; i++) {
      for(int j = 0; j < m.cols; j++) {
        mapBuilder.append(m.data[i][j]);
      }
      mapBuilder.append('\n');
    }

    // Set all other player's locations as their initial
    for(int i = 0; i < userList.size(); i++) {
      UserRec userData = userList.get(i);
      mapBuilder.setCharAt(userData.row * (m.cols+1) + userData.col, userData.name.charAt(0));
    }

    // Add the current player's location as an @
    mapBuilder.setCharAt(playerData.row * (m.cols+1) + playerData.col, '@');

    mapBuilder.append("gold: " + playerData.gold);
    
    return mapBuilder.toString();
  }

  /** Function to take gold under the player. */
  private static synchronized String take(UserRec playerData) {
    // Check that the location of the player has gold under it
    if(m.data[playerData.row][playerData.col] == '$') {
      // Pick up the gold
      playerData.gold++;
      m.data[playerData.row][playerData.col] = '.';

      return "";
    }
    
    return "Invalid command";
  }

  /** Function to drop gold under the player. */
  private static synchronized String drop(UserRec playerData) {
    // Check that the location of the player is empty and the player has one gold at least
    if(m.data[playerData.row][playerData.col] == '.' && playerData.gold > 0) {
      // Drop the gold
      playerData.gold--;
      m.data[playerData.row][playerData.col] = '$';

      return "";
    }
    
    return "Invalid command";
  }

  private static class handlerThread implements Runnable {
    // This thread's assigned client socket.
    Socket sock;

    // Record the client socket we are supposed to used.
    public handlerThread( Socket sock ) {
      this.sock = sock;
    }

  
  
    /** Handle interaction with a client. */
    public void run() {
      try {
        // Get formatted input/output streams for this thread.  These
        // can read and write strings, arrays of bytes, ints, lots of
        // things.
        DataOutputStream output = new DataOutputStream( sock.getOutputStream() );
        DataInputStream input = new DataInputStream( sock.getInputStream() );
        
        // Get the username.
        String username = input.readUTF();

        // Make a random sequence of bytes to use as a challenge string.
        Random rand = new Random();
        byte[] challenge = new byte [ 16 ];
        rand.nextBytes( challenge );

        // Make a session key for communiating over AES.  We use it later, if the
        // client successfully authenticates.
        byte[] sessionKey = new byte [ 16 ];
        rand.nextBytes( sessionKey );

        // Find this user.  We don't need to synchronize here, since
        // the set of users never changes.
        UserRec rec = null;
        for ( int i = 0; rec == null && i < userList.size(); i++ )
          if ( userList.get( i ).name.equals( username ) )
            rec = userList.get( i );

        // Did we find a record for this user?
        if ( rec != null ) {
          // Make sure the client encrypted the challenge properly.

          // RSA is used to establish the session key for ASA which will be used for commands.

          // Create RSA encrypter and decrypter objects.
          Cipher RSADecrypter = Cipher.getInstance( "RSA" );
          RSADecrypter.init( Cipher.DECRYPT_MODE, rec.publicKey );
            
          Cipher RSAEncrypter = Cipher.getInstance( "RSA" );
          RSAEncrypter.init( Cipher.ENCRYPT_MODE, rec.publicKey );
            
          // Send the client the challenge.
          putMessage( output, challenge );
            
          // Get back the client's encrypted challenge.
          byte[] challengeEncrypted = getMessage(input);

          // Make sure the client properly encrypted the challenge.
          byte[] challengeDecrypted = RSADecrypter.doFinal(challengeEncrypted);

          for(int i = 0; i < 16; i++) {
            if(Byte.compare(challenge[i], challengeDecrypted[i]) != 0) {
              throw new GeneralSecurityException("RSA challenge failed");
            }
          }
            
          // Send the client our session key (encrytped with RSA)
          putMessage(output, RSAEncrypter.doFinal(sessionKey));

          // Make a key object from the session key byte array.
          SecretKey sessionKeyObj = new SecretKeySpec( sessionKey, "AES" );

          // Make AES decrypter and encrypter ciphers.
          Cipher AESDecrypter = Cipher.getInstance("AES/ECB/PKCS5Padding");
          AESDecrypter.init(Cipher.DECRYPT_MODE, sessionKeyObj);
          Cipher AESEncrypter = Cipher.getInstance("AES/ECB/PKCS5Padding");
          AESEncrypter.init(Cipher.ENCRYPT_MODE, sessionKeyObj);

          // Get the first client command and make a scanner to extract its fields.
          String request = new String( AESDecrypter.doFinal( getMessage( input ) ) );

          // Until the client enters the quit command.
          while ( ! request.equals( "quit" ) && ! request.equals( "q" ) ) {
            StringBuilder reply = new StringBuilder();

            // Find which index in userList this user's records are in
            int userIdx = 0;
            while(userList.get(userIdx).name.compareTo(username) != 0)
              userIdx++;

            // Determine which command the user wants to execute
            if(request.equals("north") || request.equals("n")) {
              reply.append(north(userList.get(userIdx)));
            } else if(request.equals("west") || request.equals("w")) {
              reply.append(west(userList.get(userIdx)));
            }else if(request.equals("south") || request.equals("s")) {
              reply.append(south(userList.get(userIdx)));
            }else if(request.equals("east") || request.equals("e")) {
              reply.append(east(userList.get(userIdx)));
            }else if(request.equals("map") || request.equals("m")) {
              reply.append(map(userList.get(userIdx)));
            }else if(request.equals("take") || request.equals("t")) {
              reply.append(take(userList.get(userIdx)));
            }else if(request.equals("drop") || request.equals("d")) {
              reply.append(drop(userList.get(userIdx)));
            } else {
              reply.append( "Invalid command" );
            }
                
            // For now, just send back a copy of the request.
            if(reply.length() != 0) {
              reply.append( "\n" );
            }

            // Send the reply back to our client
            putMessage( output, AESEncrypter.doFinal( reply.toString().getBytes() ) );
                
            // Get the next command.
            request = new String( AESDecrypter.doFinal( getMessage( input ) ) );
          }
        }
      } catch ( IOException e ) {
        System.out.println( "IO Error: " + e );
      } catch( GeneralSecurityException e ){
        System.err.println( "Encryption error: " + e );
      } finally {
        try {
          // Close the socket on the way out.
          sock.close();
        } catch ( Exception e ) {
        }
      }
    }

  }

  /** Instance method for main, so we can access non-static fields. */
  private void runMain( String[] args ) {
    if ( args.length != 1 )
      usage();

    ServerSocket serverSocket = null;

    // Create an input scanner for the map file
    Scanner mapInput;
    try {
      mapInput = new Scanner( new File( args[0] ) );
    } catch (FileNotFoundException e) {
      mapInput = (Scanner)null;
      usage();
    }

    // One-time setup.
    try {
      // Read the public keys for all the users.
      readUsers();

      // Open a socket for listening.
      serverSocket = new ServerSocket( PORT_NUMBER );
    } catch( Exception e ){
      System.err.println( "Can't initialize server: " + e );
      e.printStackTrace();
      System.exit( 1 );
    }

    try {
      // Fill in the MapRec object

      // Get the rows and cols
      m.rows = mapInput.nextInt();
      m.cols = mapInput.nextInt();

      // Check that the rows and cols are valid
      if(m.rows <= 0 || m.cols <= 0) {
        System.err.println( "Invalid map file" );
        System.exit( 1 );
      }

      // Get the data array
      m.data = new char[m.rows][m.cols];
      mapInput.useDelimiter("");
      // Discard the newline
      mapInput.next();

      // Fill in the data
      for(int i = 0; i < m.rows; i++) {
        for(int j = 0; j < m.cols; j++) {
          m.data[i][j] = mapInput.next().charAt(0);

          // Check that the data is valid
          if(m.data[i][j] != '#' && m.data[i][j] != '$' && m.data[i][j] != '.'){
            System.err.println( "Invalid map file" );
            System.exit( 1 );
          }
        }
        // Discard the newline
        mapInput.next();
      }

      // Read the user data

      mapInput.useDelimiter(" |\n");
      for(int i = 0; i < userList.size(); i++) {
        UserRec currentRecord = userList.get(i);

        // If the expected name(passwd.txt) and actual name(map file) do not match, exit with an error message
        if( ! mapInput.next().equals(currentRecord.name)) {
          System.err.println( "Invalid map file" );
          System.exit( 1 );
        }
        currentRecord.row = mapInput.nextInt();
        currentRecord.col = mapInput.nextInt();
        currentRecord.gold = mapInput.nextInt();

        // Check the user data (gold >= 0, not off edges, not on a wall)
        if(currentRecord.gold < 0 || currentRecord.col < 0 || currentRecord.col >= m.cols || 
            currentRecord.row < 0 || currentRecord.row >= m.rows || m.data[currentRecord.row][currentRecord.col] == '#') {
              System.err.println( "Invalid map file" );
              System.exit( 1 );
        }
      }

      // Check that two users aren't occupying the same space
      for(int i = 0; i < userList.size(); i++) {
        for(int j = 0; j < userList.size(); j++) {
          if( (!userList.get(i).name.equals(userList.get(j).name)) && userList.get(i).row == userList.get(j).row && userList.get(i).col == userList.get(j).col) {
            System.err.println( "Invalid map file" );
            System.exit( 1 );
          }
        }
      }
    } catch (Exception e) {
      System.err.println( "Invalid map file" );
      System.exit( 1 );
    }
    
    // Close the map input scanner
    mapInput.close();
    
    
     
    // Keep trying to accept new connections and serve them.
    while( true ){
      try {
        // Try to get a new client connection.
        Socket sock = serverSocket.accept();

        // Create a new thread to handle the client.
        Runnable h = new handlerThread(sock);
        Thread t = new Thread(h);

        // Start the thread
        t.start();
      } catch( IOException e ){
        System.err.println( "Failure accepting client " + e );
      }
    }
  }

  public static void main( String[] args ) {
    // Make a server object, so we can use non-static fields and methods.
    Server server = new Server();
    server.runMain( args );
  }
}
