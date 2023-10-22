import {
    Home as HomeView,
    SignIn as SignInView,
    ProcessLogin as ProcessLoginView,
    Dashboard as DashboardView,
} from '../views';

const routes = [
    {
        path: '/',
        renderer: (params = {}) => <HomeView {...params} />,
    },
    {
        path: '/sign-in',
        renderer: (params = {}) => <SignInView {...params} />,
    },
    {
        path: '/process/auth/login',
        renderer: (params = {}) => <ProcessLoginView {...params} />,
    },
    {
        path: '/dashboard',
        renderer: (params = {}) => <DashboardView {...params} />,
    },
];

export default routes;