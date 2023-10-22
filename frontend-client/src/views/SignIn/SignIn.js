
export const SignIn = () => {
    return (
        <div className="container">
            <form className="form-signin" method="post" action="http://127.0.0.1:9000/login">
                <h2 className="form-signin-heading">Please sign in</h2>
                <p>
                    <label htmlFor="username" className="sr-only">Username</label>
                    <input type="text" id="username"
                           name="username"
                           className="form-control"
                           placeholder="Username"
                           required autoFocus />
                </p>
                <p>
                    <label htmlFor="password" className="sr-only">Password</label>
                    <input type="password"
                           id="password"
                           name="password"
                           className="form-control"
                           placeholder="Password"
                           required />
                </p>
                {/*<input name="_csrf" type="hidden" value={xsrfToken} />*/}
                <button className="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
            </form>
        </div>
    );
}