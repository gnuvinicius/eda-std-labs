export async function validateSession() {
  try {
    // const res = await fetch(process.env.NEXT_PUBLIC_API_URL + "/auth/me", {
    //   credentials: "include",
    // });

    // if (!res.ok) return null;

    // return await res.json();

    return { id: 1, name: "Vinicius Claudino", email: "gnu.vinicius@gmail.com"}
  } catch {
    return null;
  }
}
