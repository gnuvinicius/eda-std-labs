import Sidebar from "@/components/sidebar/Sidebar";
import Header from "@/components/header/Header";
import { validateSession } from "@/lib/auth";
import { redirect } from "next/navigation";

export default async function DashboardLayout({ children }: { children: React.ReactNode }) {
  const user = await validateSession();

  if (!user) redirect("/login");

  return (
    <div className="flex h-screen">
      <Sidebar />

      <div className="flex flex-1 flex-col">
        <Header user={user} />
        <main className="p-6 overflow-y-auto">{children}</main>
      </div>
    </div>
  );
}
