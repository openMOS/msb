/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.milo.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509IdentityProvider implements IdentityProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final X509Certificate certificate;
    private final PrivateKey privateKey;

    public X509Certificate getCertificate() {
        return certificate;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public X509IdentityProvider(X509Certificate certificate, PrivateKey privateKey) {
        this.certificate = certificate;
        this.privateKey = privateKey;
    }

    public X509IdentityProvider(String certificate, String privateKey) {
        this.certificate = loadCertificateFromDerFile(certificate);

        Security.addProvider(new BouncyCastleProvider());
        KeyFactory kf;
        PrivateKey privateKeyTmp = null;
        try {
            kf = KeyFactory.getInstance("RSA", "BC");
            privateKeyTmp = loadPrivateKeyFromPemFile(kf, privateKey);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | IOException e) {
            e.printStackTrace();
        }
        this.privateKey = privateKeyTmp;

    }

    @Override
    public Tuple2<UserIdentityToken, SignatureData> getIdentityToken(EndpointDescription endpoint,
            ByteString serverNonce) throws Exception {
        UserTokenPolicy tokenPolicy = Arrays.stream(endpoint.getUserIdentityTokens())
                .filter(t -> t.getTokenType() == UserTokenType.Certificate).findFirst()
                .orElseThrow(() -> new Exception("no x509 certificate token policy found"));
        String policyId = tokenPolicy.getPolicyId();
        SecurityPolicy securityPolicy = SecurityPolicy.Basic256;
        String securityPolicyUri = tokenPolicy.getSecurityPolicyUri();
        try {
            if (securityPolicyUri != null && !securityPolicyUri.isEmpty()) {
                securityPolicy = SecurityPolicy.fromUri(securityPolicyUri);
            } else {
                securityPolicyUri = endpoint.getSecurityPolicyUri();
                securityPolicy = SecurityPolicy.fromUri(securityPolicyUri);
            }
        } catch (Throwable t) {
            logger.warn("Error parsing SecurityPolicy for uri={}", securityPolicyUri);
        }
        X509IdentityToken token = new X509IdentityToken(policyId, ByteString.of(certificate.getEncoded()));
        SignatureData signatureData;
        ByteString serverCertificate = endpoint.getServerCertificate();
        byte[] serverCertificateBytes = serverCertificate.isNotNull() ? serverCertificate.bytes() : new byte[0];
        byte[] serverNonceBytes = serverNonce.isNotNull() ? serverNonce.bytes() : new byte[0];
        assert serverCertificateBytes != null;
        assert serverNonceBytes != null;
        byte[] signature = SignatureUtil.sign(securityPolicy.getAsymmetricSignatureAlgorithm(), privateKey,
                ByteBuffer.wrap(serverCertificateBytes), ByteBuffer.wrap(serverNonceBytes));
        signatureData = new SignatureData(securityPolicy.getAsymmetricSignatureAlgorithm().getUri(),
                ByteString.of(signature));
        return new Tuple2<>(token, signatureData);
    }


    private static X509Certificate loadCertificateFromDerFile(String filename) {
        InputStream in;
        X509Certificate cert = null;
        try {
            in = new FileInputStream(filename);

            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) factory.generateCertificate(in);

            /*
              byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
                X509EncodedKeySpec spec =
      new X509EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePublic(spec); 
             */
        } catch (FileNotFoundException | CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cert;
    }

    private static PrivateKey loadPrivateKeyFromPemFile(KeyFactory factory, String filename)
            throws InvalidKeySpecException, FileNotFoundException, IOException, NoSuchAlgorithmException {
        PemFile pemFile = new PemFile(filename);
        byte[] content = pemFile.getPemObject().getContent();
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
        System.out.println(privKeySpec.getFormat());

        // PKCS8EncodedKeySpec spec =
        // new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(privKeySpec);
        //  return factory.generatePrivate(privKeySpec);
    }

}
